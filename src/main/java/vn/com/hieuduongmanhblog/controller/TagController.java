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
    public ResponseDTO getAllTags() {
        List<Tag> tags = tagService.getAllTags();
        return new ResponseDTO(HttpStatus.OK, "Get All Tags Successful", LocalDateTime.now(), tags);
    }

    @GetMapping("/tags/{tagId}")
    public ResponseDTO findTagById(@PathVariable int tagId) {
        Tag givenTag = tagService.findTagById(tagId);
        return new ResponseDTO(HttpStatus.OK, "Get Tag By ID Successful", LocalDateTime.now(), givenTag);
    }

    @PostMapping("/tags")
    public ResponseDTO createNewTag(@RequestBody Tag newTag) {
        Tag createdTag = tagService.createTag(newTag);
        return new ResponseDTO(HttpStatus.CREATED, "Create New Tag Successful", LocalDateTime.now(), createdTag);
    }

    @PutMapping("/tags/{tagId}")
    public ResponseDTO updateExistingTag(@PathVariable int tagId, @RequestBody Tag newTag) {
        Tag updatedTag = tagService.updateTagById(tagId, newTag);
        return new ResponseDTO(HttpStatus.OK, "Update Tag Successful", LocalDateTime.now(), updatedTag);
    }

    @DeleteMapping("/tags/{tagId}")
    public ResponseDTO deleteUserById(@PathVariable int tagId) {
        tagService.deleteTagById(tagId);
        return new ResponseDTO(HttpStatus.OK, "Delete Tag Successful", LocalDateTime.now());
    }
}
