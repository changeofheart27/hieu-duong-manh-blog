package com.nashtech.hieuduongmanhblog.service;

import com.nashtech.hieuduongmanhblog.entity.Tag;
import com.nashtech.hieuduongmanhblog.exception.ResourceNotFoundException;
import com.nashtech.hieuduongmanhblog.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class TagServiceImpl implements TagService {
    private final TagRepository tagRepository;

    @Autowired
    public TagServiceImpl(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Override
    public List<Tag> getAllTags() {
        return tagRepository.findAll();
    }

    @Override
    public Tag findTagById(int tagId) {
        Optional<Tag> optionalTag = tagRepository.findById(tagId);
        if (optionalTag.isPresent()) {
            return optionalTag.get();
        } else throw new ResourceNotFoundException("Could not find Tag with id - " + tagId);
    }

    @Override
    @Transactional
    public Tag createTag(Tag newTag) {
        // setting id to 0 to avoid passing new post with existing id inside database
        // and a save of new item instead of updating current one
        newTag.setId(0);
        return tagRepository.save(newTag);
    }

    @Override
    @Transactional
    public Tag updateTagById(int tagId, Tag newTag) {
        Tag tagToUpdate = findTagById(tagId);
        tagToUpdate.setName(newTag.getName());
        return tagRepository.save(newTag);
    }

    @Override
    @Transactional
    public void deleteTagById(int tagId) {
        tagRepository.deleteById(tagId);
    }
}
