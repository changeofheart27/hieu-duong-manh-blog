package vn.com.hieuduongmanhblog.controller;

import vn.com.hieuduongmanhblog.dto.PostDTO;
import vn.com.hieuduongmanhblog.dto.ResponseDTO;
import vn.com.hieuduongmanhblog.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
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
    public ResponseDTO getAllPosts() {
        List<PostDTO> posts = postService.getAllPosts();
        return new ResponseDTO(HttpStatus.OK, "Get All Posts Successful", LocalDateTime.now(), posts);
    }

    @GetMapping("/posts/{postId}")
    public ResponseDTO findPostById(@PathVariable int postId) {
        PostDTO givenPost = postService.findPostById(postId);
        return new ResponseDTO(HttpStatus.OK, "Get Post By ID Successful", LocalDateTime.now(), givenPost);
    }

    @GetMapping(value = "/posts", params = {"username"})
    public ResponseDTO findPostsByUser(@RequestParam(value = "username") String username) {
        List<PostDTO> givenPosts = postService.findPostsByUser(username);
        return new ResponseDTO(HttpStatus.OK, "Get Posts By Username Successful", LocalDateTime.now(), givenPosts);
    }

    @PostMapping("/posts")
    public ResponseDTO createNewPost(@RequestBody PostDTO newPost) {
        PostDTO createdPost = postService.createPost(newPost);
        return new ResponseDTO(HttpStatus.CREATED, "Create New Post Successful", LocalDateTime.now(), createdPost);
    }

    @PutMapping("/posts/{postId}")
    public ResponseDTO updateExistingPost(@PathVariable int postId, @RequestBody PostDTO newPost) {
        PostDTO updatedPost = postService.updatePostById(postId, newPost);
        return new ResponseDTO(HttpStatus.OK, "Update Post Successful", LocalDateTime.now(), updatedPost);
    }

    @DeleteMapping("/posts/{postId}")
    public ResponseDTO deleteUserById(@PathVariable int postId) {
        postService.deletePostById(postId);
        return new ResponseDTO(HttpStatus.OK, "Delete Post Successful", LocalDateTime.now());
    }
}
