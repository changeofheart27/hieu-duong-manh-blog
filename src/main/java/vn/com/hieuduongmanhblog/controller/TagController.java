package vn.com.hieuduongmanhblog.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import vn.com.hieuduongmanhblog.dto.ResponseDTO;
import vn.com.hieuduongmanhblog.dto.TagDTO;
import vn.com.hieuduongmanhblog.entity.Tag;
import vn.com.hieuduongmanhblog.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@io.swagger.v3.oas.annotations.tags.Tag(name = "Tag Controller", description = "Endpoints for Tag Operations")
@RestController
@RequestMapping("/api/v1/tags")
public class TagController {
    private final TagService tagService;

    @Autowired
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @ApiResponse(
            responseCode = "200",
            description = "Get All Tags Successful",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ResponseDTO.class)
            )
    )
    @Operation(summary = "Get All Tags", description = "Get All Tags With Pagination Support")
    @GetMapping
    public ResponseEntity<ResponseDTO> getAllTags() {
        List<TagDTO> tags = tagService.getAllTags();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDTO(HttpStatus.OK.value(), "Get All Tags Successful", LocalDateTime.now(), tags));
    }

    @ApiResponse(
            responseCode = "200",
            description = "Get Tag By ID Successful",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ResponseDTO.class)
            )
    )
    @Operation(summary = "Get Tag By ID", description = "Get Tag With Specific ID")
    @GetMapping("/{tagId}")
    public ResponseEntity<ResponseDTO> findTagById(@PathVariable int tagId) {
        TagDTO givenTag = tagService.findTagById(tagId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDTO(HttpStatus.OK.value(), "Get Tag By ID Successful", LocalDateTime.now(), givenTag));
    }

    @ApiResponse(
            responseCode = "201",
            description = "Create New Tag Successful",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ResponseDTO.class)
            )
    )
    @Operation(summary = "Create New Tag", description = "Create New Tag With Request Payload")
    @PostMapping
    public ResponseEntity<ResponseDTO> createNewTag(@RequestBody TagDTO newTag) {
        TagDTO createdTag = tagService.createTag(newTag);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDTO(HttpStatus.CREATED.value(), "Create New Tag Successful", LocalDateTime.now(), createdTag));
    }

    @ApiResponse(
            responseCode = "200",
            description = "Delete Tag Successful",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ResponseDTO.class)
            )
    )
    @Operation(summary = "Delete Tag", description = "Delete Tag With Specific ID")
    @DeleteMapping("/{tagId}")
    public ResponseEntity<ResponseDTO> deleteTagById(@PathVariable int tagId) {
        tagService.deleteTagById(tagId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDTO(HttpStatus.OK.value(), "Delete Tag Successful", LocalDateTime.now()));
    }
}
