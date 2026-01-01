package vn.com.hieuduongmanhblog.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name = "Post Controller", description = "Endpoints for Post Operations")
@RestController
@RequestMapping("/api/v1/posts")
public class PostController {
    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @ApiResponse(responseCode = "200", description = "Get All Posts Successful")
    @Operation(summary = "Get All Posts", description = "Get All Posts With Pagination Support")
    @GetMapping
    public ResponseEntity<ResponseDTO> getAllPosts(
            @RequestParam(value = "page", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(value = "size", defaultValue = "5", required = false) int pageSize
    ) {
        Page<PostDTO> posts = postService.getAllPosts(pageNumber, pageSize);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDTO(HttpStatus.OK.value(), "Get All Posts Successful", LocalDateTime.now(), posts));
    }

    @ApiResponse(responseCode = "200", description = "Get Post By ID Successful")
    @Operation(summary = "Get Post By ID", description = "Get Post With Specific ID")
    @GetMapping("/{postId}")
    public ResponseEntity<ResponseDTO> findPostById(@PathVariable int postId) {
        PostDTO givenPost = postService.findPostById(postId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDTO(HttpStatus.OK.value(), "Get Post By ID Successful", LocalDateTime.now(), givenPost));
    }

    @ApiResponse(responseCode = "200", description = "Get Posts By username Successful")
    @Operation(summary = "Get Posts By Username", description = "Get Posts With Specific Username")
    @GetMapping("/username")
    public ResponseEntity<ResponseDTO> findPostsByUser(
            @RequestParam(value = "username") String username,
            @RequestParam(value = "page", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(value = "size", defaultValue = "5", required = false) int pageSize
    ) {
        Page<PostDTO> givenPosts = postService.findPostsByUser(username, pageNumber, pageSize);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDTO(HttpStatus.OK.value(), "Get Posts By username Successful", LocalDateTime.now(), givenPosts));
    }

    @ApiResponse(responseCode = "201", description = "Create New Post Successful")
    @Operation(summary = "Create New Post", description = "Create New Post With Request Payload")
    @PostMapping
    public ResponseEntity<ResponseDTO> createNewPost(@Valid @RequestBody PostDTO newPost) {
        PostDTO createdPost = postService.createPost(newPost);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDTO(HttpStatus.CREATED.value(), "Create New Post Successful", LocalDateTime.now(), createdPost));
    }

    @ApiResponse(responseCode = "200", description = "Update Post Successful")
    @Operation(summary = "Update Post", description = "Update Post With Specific ID And Request Payload")
    @PutMapping("/{postId}")
    public ResponseEntity<ResponseDTO> updateExistingPost(@PathVariable int postId, @Valid @RequestBody PostDTO newPost) {
        PostDTO updatedPost = postService.updatePostById(postId, newPost);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDTO(HttpStatus.OK.value(), "Update Post Successful", LocalDateTime.now(), updatedPost));
    }

    @ApiResponse(responseCode = "200", description = "Delete Post Successful")
    @Operation(summary = "Delete Post", description = "Delete Post With Specific ID")
    @DeleteMapping("/{postId}")
    public ResponseEntity<ResponseDTO> deleteUserById(@PathVariable int postId) {
        postService.deletePostById(postId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDTO(HttpStatus.OK.value(), "Delete Post Successful", LocalDateTime.now()));
    }
}
