package com.nashtech.hieuduongmanhblog.service;

import com.nashtech.hieuduongmanhblog.entity.Tag;

import java.util.List;

public interface TagService {
    List<Tag> getAllTags();

    Tag findTagById(int tagId);

    Tag createTag(Tag newTag);

    Tag updateTagById(int tagId, Tag newTag);

    void deleteTagById(int tagId);
}
