package com.nashtech.hieuduongmanhblog.controller;

import com.nashtech.hieuduongmanhblog.entity.Post;
import com.nashtech.hieuduongmanhblog.entity.User;
import com.nashtech.hieuduongmanhblog.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class PostController {
    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping(value = "/posts", params = {})
    public ResponseEntity<List<Post>> getAllPosts() {
        List<Post> posts = postService.getAllPosts();
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    @GetMapping("/posts/{postId}")
    public ResponseEntity<Post> findPostById(@PathVariable int postId) {
        Post givenPost = postService.findPostById(postId);
        return new ResponseEntity<>(givenPost, HttpStatus.OK);
    }

    @GetMapping(value = "/posts", params = {"username"})
    public ResponseEntity<List<Post>> findPostsByUser(@RequestParam(value = "username") String username) {
        List<Post> givenPosts = postService.findPostsByUser(username);
        return new ResponseEntity<>(givenPosts, HttpStatus.OK);
    }

    @PostMapping("/posts")
    public ResponseEntity<Post> createNewPost(@RequestBody Post newPost) {
        Post createdPost = postService.createPost(newPost);
        return new ResponseEntity<>(createdPost, HttpStatus.CREATED);
    }

    @PutMapping("/posts/{postId}")
//    @PreAuthorize(value = admin)
    public ResponseEntity<Post> updateExistingPost(@PathVariable int postId, @RequestBody Post newPost) {
        Post updatedPost = postService.updatePostById(postId, newPost);
        return new ResponseEntity<>(updatedPost, HttpStatus.OK);
    }

    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<Void> deleteUserById(@PathVariable int postId) {
        postService.deletePostById(postId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
