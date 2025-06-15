package vn.com.hieuduongmanhblog.controller;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import vn.com.hieuduongmanhblog.dto.PostDTO;
import vn.com.hieuduongmanhblog.dto.ResponseDTO;
import vn.com.hieuduongmanhblog.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1")
public class PostController {
    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping(value = "/posts")
    public ResponseEntity<ResponseDTO> getAllPosts(
            @RequestParam(value = "page", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(value = "size", defaultValue = "5", required = false) int pageSize
    ) {
        Page<PostDTO> posts = postService.getAllPosts(pageNumber, pageSize);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDTO(HttpStatus.OK.value(), "Get All Posts Successful", LocalDateTime.now(), posts));
    }

    @GetMapping("/posts/{postId}")
    public ResponseEntity<ResponseDTO> findPostById(@PathVariable int postId) {
        PostDTO givenPost = postService.findPostById(postId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDTO(HttpStatus.OK.value(), "Get Post By ID Successful", LocalDateTime.now(), givenPost));
    }

    @GetMapping(value = "/posts", params = {"username"})
    public ResponseEntity<ResponseDTO> findPostsByUser(
            @RequestParam(value = "username") String username,
            @RequestParam(value = "page", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(value = "size", defaultValue = "5", required = false) int pageSize
    ) {
        Page<PostDTO> givenPosts = postService.findPostsByUser(username, pageNumber, pageSize);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDTO(HttpStatus.OK.value(), "Get Posts By Username Successful", LocalDateTime.now(), givenPosts));
    }

    @PostMapping("/posts")
    public ResponseEntity<ResponseDTO> createNewPost(@Valid @RequestBody PostDTO newPost) {
        PostDTO createdPost = postService.createPost(newPost);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDTO(HttpStatus.CREATED.value(), "Create New Post Successful", LocalDateTime.now(), createdPost));
    }

    @PutMapping("/posts/{postId}")
    public ResponseEntity<ResponseDTO> updateExistingPost(@PathVariable int postId, @Valid @RequestBody PostDTO newPost) {
        PostDTO updatedPost = postService.updatePostById(postId, newPost);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDTO(HttpStatus.OK.value(), "Update Post Successful", LocalDateTime.now(), updatedPost));
    }

    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<ResponseDTO> deleteUserById(@PathVariable int postId) {
        postService.deletePostById(postId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDTO(HttpStatus.OK.value(), "Delete Post Successful", LocalDateTime.now()));
    }
}
