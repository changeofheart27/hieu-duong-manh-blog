package com.nashtech.hieuduongmanhblog.service;

import com.nashtech.hieuduongmanhblog.entity.Post;

import java.util.List;

public interface PostService {
    List<Post> getAllPosts();

    Post findPostById(int postId);

    List<Post> findPostsByUser(String username);

    Post createPost(Post newPost);

    Post updatePostById(int postId, Post newPost);

    void deletePostById(int postId);
}
