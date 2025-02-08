package vn.com.hieuduongmanhblog.dto.mapper;

import vn.com.hieuduongmanhblog.dto.PostDTO;
import vn.com.hieuduongmanhblog.entity.Post;
import org.springframework.stereotype.Service;

@Service
public class PostMapper {
    public PostDTO toPostDTO(Post post) {
        return new PostDTO(
                post.getId(),
                post.getTitle(),
                post.getDescription(),
                post.getContent(),
                post.getCreatedAt(),
                post.getUpdatedAt(),
                post.getUser().getUsername()
        );
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
