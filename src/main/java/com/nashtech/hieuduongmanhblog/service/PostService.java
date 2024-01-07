package com.nashtech.hieuduongmanhblog.service;

import com.nashtech.hieuduongmanhblog.dto.PostDTO;

import java.util.List;

public interface PostService {
    List<PostDTO> getAllPosts();

    PostDTO findPostById(int postId);

    List<PostDTO> findPostsByUser(String username);

    PostDTO createPost(PostDTO newPost);

    PostDTO updatePostById(int postId, PostDTO newPost);

    void deletePostById(int postId);
}
