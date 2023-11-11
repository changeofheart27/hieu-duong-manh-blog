package com.nashtech.hieuduongmanhblog.service;

import com.nashtech.hieuduongmanhblog.entity.Post;
import com.nashtech.hieuduongmanhblog.entity.User;
import com.nashtech.hieuduongmanhblog.exception.ResourceNotFoundException;
import com.nashtech.hieuduongmanhblog.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final UserService userService;

    @Autowired
    public PostServiceImpl(PostRepository postRepository, UserService userService) {
        this.postRepository = postRepository;
        this.userService = userService;
    }

    @Override
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    @Override
    public Post findPostById(int postId) {
        Optional<Post> optionalPost = postRepository.findById(postId);
        if (optionalPost.isPresent()) {
            return optionalPost.get();
        } else throw new ResourceNotFoundException("Could not find Post with id - " + postId);
    }

    @Override
    public List<Post> findPostsByUser(String username) {
        return postRepository.findByUser_Username(username);
    }

    @Override
    @Transactional
    public Post createPost(Post newPost) {
        // setting id to 0 to avoid passing new post with existing id inside database
        // and a save of new item instead of updating current one
        newPost.setId(0);

        UserDetails currentUserInfo = getCurrentLoggedInUser();
        User currentUser = userService.findUserByUsername(currentUserInfo.getUsername());
        newPost.setUser(currentUser);
        newPost.setCreatedAt(LocalDate.now());
        return postRepository.save(newPost);
    }

    @Override
    @Transactional
    public Post updatePostById(int postId, Post newPost) {
        Post postToUpdate = findPostById(postId);
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

        if (newPost.getTitle() != null) {
            postToUpdate.setTitle(newPost.getTitle());
        }
        if (newPost.getDescription() != null) {
            postToUpdate.setDescription(newPost.getDescription());
        }
        if (newPost.getContent() != null) {
            postToUpdate.setContent(newPost.getContent());
        }
        return postRepository.save(postToUpdate);
    }

    @Override
    @Transactional
    public void deletePostById(int postId) {
        Post postToDelete = findPostById(postId);
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
        postRepository.deleteById(postId);
    }

    private UserDetails getCurrentLoggedInUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (UserDetails) auth.getPrincipal();
    }
}
