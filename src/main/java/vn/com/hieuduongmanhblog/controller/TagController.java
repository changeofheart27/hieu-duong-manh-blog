package vn.com.hieuduongmanhblog.controller;

import vn.com.hieuduongmanhblog.dto.ResponseDTO;
import vn.com.hieuduongmanhblog.entity.Tag;
import vn.com.hieuduongmanhblog.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class TagController {
    private final TagService tagService;

    @Autowired
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping("/tags")
    public ResponseEntity<ResponseDTO> getAllTags() {
        List<Tag> tags = tagService.getAllTags();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDTO(HttpStatus.OK.value(), "Get All Tags Successful", LocalDateTime.now(), tags));
    }

    @GetMapping("/tags/{tagId}")
    public ResponseEntity<ResponseDTO> findTagById(@PathVariable int tagId) {
        Tag givenTag = tagService.findTagById(tagId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDTO(HttpStatus.OK.value(), "Get Tag By ID Successful", LocalDateTime.now(), givenTag));
    }

    @PostMapping("/tags")
    public ResponseEntity<ResponseDTO> createNewTag(@RequestBody Tag newTag) {
        Tag createdTag = tagService.createTag(newTag);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDTO(HttpStatus.CREATED.value(), "Create New Tag Successful", LocalDateTime.now(), createdTag));
    }

    @PutMapping("/tags/{tagId}")
    public ResponseEntity<ResponseDTO> updateExistingTag(@PathVariable int tagId, @RequestBody Tag newTag) {
        Tag updatedTag = tagService.updateTagById(tagId, newTag);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDTO(HttpStatus.OK.value(), "Update Tag Successful", LocalDateTime.now(), updatedTag));
    }

    @DeleteMapping("/tags/{tagId}")
    public ResponseEntity<ResponseDTO> deleteTagById(@PathVariable int tagId) {
        tagService.deleteTagById(tagId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDTO(HttpStatus.OK.value(), "Delete Tag Successful", LocalDateTime.now()));
    }
}
