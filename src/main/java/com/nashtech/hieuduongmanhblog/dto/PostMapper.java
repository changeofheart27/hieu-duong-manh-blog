package com.nashtech.hieuduongmanhblog.dto;

import com.nashtech.hieuduongmanhblog.entity.Post;
import com.nashtech.hieuduongmanhblog.entity.User;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class PostMapper {
    private final UserMapper userMapper;

    public PostMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public PostDTO toPostDTO(Post post) {
        PostDTO postDTO = new PostDTO(
                post.getId(),
                post.getTitle(),
                post.getDescription(),
                post.getContent(),
                post.getCreatedAt()
        );
        User postAuthor = post.getUser();
        if (postAuthor != null) {
            postDTO.setUserDTO(userMapper.toUserDTO(post.getUser()));
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
