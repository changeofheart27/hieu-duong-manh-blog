package vn.com.hieuduongmanhblog.dto.mapper;

import vn.com.hieuduongmanhblog.dto.PostDTO;
import vn.com.hieuduongmanhblog.entity.Post;
import vn.com.hieuduongmanhblog.entity.User;
import org.springframework.stereotype.Service;

@Service
public class PostMapper {
    private final UserMapper userMapper;

    public PostMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public PostDTO toPostDTO(Post post) {
        PostDTO postDTO = new PostDTO();
        postDTO.setId(post.getId());
        postDTO.setTitle(post.getTitle());
        postDTO.setDescription(post.getDescription());
        postDTO.setContent(post.getContent());
        postDTO.setCreatedAt(post.getCreatedAt());
        User user = post.getUser();
        if (user != null) {
            postDTO.setPostAuthor(user.getUsername());
        }
        return postDTO;
    }

    public Post toPost(PostDTO postDTO) {
        return new Post(
                postDTO.getId(),
                postDTO.getTitle(),
                postDTO.getDescription(),
                postDTO.getContent()
        );
    }

    public Post updatePostFromPostDTO(PostDTO postDTO, Post existingPost) {
        if (postDTO.getTitle() != null && postDTO.getTitle().length() != 0) {
            existingPost.setTitle(postDTO.getTitle());
        }
        if (postDTO.getDescription() != null && postDTO.getDescription().length() != 0) {
            existingPost.setDescription(postDTO.getDescription());
        }
        if (postDTO.getContent() != null && postDTO.getContent().length() != 0) {
            existingPost.setContent(postDTO.getContent());
        }

        return existingPost;
    }
}
