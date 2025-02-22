package vn.com.hieuduongmanhblog.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import vn.com.hieuduongmanhblog.dto.PostDTO;
import vn.com.hieuduongmanhblog.dto.mapper.PostMapper;
import vn.com.hieuduongmanhblog.entity.Post;
import vn.com.hieuduongmanhblog.entity.User;
import vn.com.hieuduongmanhblog.exception.ResourceNotFoundException;
import vn.com.hieuduongmanhblog.repository.PostRepository;
import vn.com.hieuduongmanhblog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.com.hieuduongmanhblog.service.PostService;
import vn.com.hieuduongmanhblog.service.UserService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PostMapper postMapper;

    @Autowired
    public PostServiceImpl(PostRepository postRepository, UserRepository userRepository, PostMapper postMapper) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.postMapper = postMapper;
    }

    @Override
    public Page<PostDTO> getAllPosts(int pageNumber, int pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        Page<Post> postsPage = postRepository.findAll(pageRequest);
        List<PostDTO> postDTOList = postsPage
                .getContent()
                .stream()
                .map(postMapper::toPostDTO)
                .toList();

        return new PageImpl<>(postDTOList, postsPage.getPageable(), postsPage.getTotalElements());
    }

    @Override
    public PostDTO findPostById(int postId) {
        Optional<Post> optionalPost = postRepository.findById(postId);
        if (optionalPost.isPresent()) {
            Post foundPost = optionalPost.get();
            return postMapper.toPostDTO(foundPost);
        } else throw new ResourceNotFoundException("Could not find Post with id - " + postId);
    }

    @Override
    public Page<PostDTO> findPostsByUser(String username, int pageNumber, int pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        Page<Post> postsPage = postRepository.findByUser_Username(username, pageRequest);
        List<PostDTO> foundPostDTOList = postsPage
                .getContent()
                .stream()
                .map(post -> postMapper.toPostDTO(post))
                .toList();

        return new PageImpl<>(foundPostDTOList, postsPage.getPageable(), postsPage.getTotalElements());
    }


    @Override
    @Transactional
    public PostDTO createPost(PostDTO newPost) {
        // setting id to 0 to avoid passing new post with existing id inside database
        // and a save of new item instead of updating current one
        newPost.setId(0);
        Post postToCreate = postMapper.toPost(newPost);
        UserDetails currentUserInfo = getCurrentLoggedInUser();
        User user = userRepository
                .findByUsername(currentUserInfo.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("Could not find User with username - " + currentUserInfo.getUsername()));
        postToCreate.setCreatedAt(LocalDateTime.now());
        postToCreate.setUser(user);

        Post createdPost = postRepository.save(postToCreate);

        return postMapper.toPostDTO(createdPost);
    }

    @Override
    @Transactional
    public PostDTO updatePostById(int postId, PostDTO newPost) {
        Post postToUpdate = postRepository
                .findById(postId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Could not find Post with id - " + postId)
                );
        UserDetails currentUserInfo = getCurrentLoggedInUser();
        String currentUserRole = currentUserInfo.getAuthorities()
                .stream()
                .map(role -> role.getAuthority())
                .collect(Collectors.joining(","));
        if (!currentUserRole.contains("ADMIN")) {
            String postAuthor = postToUpdate.getUser().getUsername();
            // check if post to update has the same username as current user
            if (!currentUserInfo.getUsername().equals(postAuthor)) {
                throw new RuntimeException("Unable to update Post by user - " + postAuthor);
            }
        }

        Post updatedPost = postRepository.save(postMapper.updatePostFromPostDTO(newPost, postToUpdate));
        return postMapper.toPostDTO(updatedPost);
    }

    @Override
    @Transactional
    public void deletePostById(int postId) {
        Post postToDelete = postRepository
                .findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Could not find Post with id - " + postId));
        UserDetails currentUserInfo = getCurrentLoggedInUser();
        String currentUserRole = currentUserInfo.getAuthorities()
                .stream()
                .map(role -> role.getAuthority())
                .collect(Collectors.joining(","));
        if (!currentUserRole.contains("ADMIN")) {
            String postAuthor = postToDelete.getUser().getUsername();
            // check if post to delete has the same username as current user
            if (!currentUserInfo.getUsername().equals(postAuthor)) {
                throw new RuntimeException("Unable to delete Post by user - " + postAuthor);
            }
        }
        postRepository.delete(postToDelete);
    }

    protected UserDetails getCurrentLoggedInUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (UserDetails) auth.getPrincipal();
    }
}
