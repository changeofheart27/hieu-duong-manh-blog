package vn.com.hieuduongmanhblog.service;

import vn.com.hieuduongmanhblog.dto.TagDTO;
import vn.com.hieuduongmanhblog.entity.Tag;

import java.util.List;

public interface TagService {
    List<TagDTO> getAllTags();

    TagDTO findTagById(int tagId);

    TagDTO createTag(TagDTO newTag);

    void deleteTagById(int tagId);
}
