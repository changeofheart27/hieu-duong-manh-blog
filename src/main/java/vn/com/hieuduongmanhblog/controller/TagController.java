package vn.com.hieuduongmanhblog.controller;

import vn.com.hieuduongmanhblog.entity.Tag;
import vn.com.hieuduongmanhblog.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<List<Tag>> getAllTags() {
        List<Tag> tags = tagService.getAllTags();
        return new ResponseEntity<>(tags, HttpStatus.OK);
    }

    @GetMapping("/tags/{tagId}")
    public ResponseEntity<Tag> findTagById(@PathVariable int tagId) {
        Tag givenTag = tagService.findTagById(tagId);
        return new ResponseEntity<>(givenTag, HttpStatus.OK);
    }

    @PostMapping("/tags")
    public ResponseEntity<Tag> createNewTag(@RequestBody Tag newTag) {
        Tag createdTag = tagService.createTag(newTag);
        return new ResponseEntity<>(createdTag, HttpStatus.CREATED);
    }

    @PutMapping("/tags/{tagId}")
    public ResponseEntity<Tag> updateExistingTag(@PathVariable int tagId, @RequestBody Tag newTag) {
        Tag updatedTag = tagService.updateTagById(tagId, newTag);
        return new ResponseEntity<>(updatedTag, HttpStatus.OK);
    }

    @DeleteMapping("/tags/{tagId}")
    public ResponseEntity<Void> deleteUserById(@PathVariable int tagId) {
        tagService.deleteTagById(tagId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
