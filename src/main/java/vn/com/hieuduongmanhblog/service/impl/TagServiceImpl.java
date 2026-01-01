package vn.com.hieuduongmanhblog.service.impl;

import vn.com.hieuduongmanhblog.dto.TagDTO;
import vn.com.hieuduongmanhblog.dto.mapper.TagMapper;
import vn.com.hieuduongmanhblog.entity.Tag;
import vn.com.hieuduongmanhblog.exception.ResourceNotFoundException;
import vn.com.hieuduongmanhblog.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.com.hieuduongmanhblog.service.TagService;

import java.util.List;
import java.util.Optional;

@Service
public class TagServiceImpl implements TagService {
    private final TagRepository tagRepository;
    private final TagMapper tagMapper;

    @Autowired
    public TagServiceImpl(TagRepository tagRepository, TagMapper tagMapper) {
        this.tagRepository = tagRepository;
        this.tagMapper = tagMapper;
    }

    @Override
    public List<TagDTO> getAllTags() {
        List<Tag> allTags = tagRepository.findAll();
        return allTags
                .stream()
                .map(tagMapper::toTagDTO)
                .toList();
    }

    @Override
    public TagDTO findTagById(int tagId) {
        Optional<Tag> optionalTag = tagRepository.findById(tagId);
        if (optionalTag.isPresent()) {
            return tagMapper.toTagDTO(optionalTag.get());
        } else throw new ResourceNotFoundException("Could not find Tag with id - " + tagId);
    }

    @Override
    @Transactional
    public TagDTO createTag(TagDTO newTag) {
        // setting id to 0 to avoid passing new post with existing id inside database
        // and a save of new item instead of updating current one
        TagDTO tagDTOWithZeroID = newTag.TagDTOWithDefaultId(0);
        Tag tagToCreate = tagMapper.toTag(tagDTOWithZeroID);
        tagRepository.save(tagToCreate);

        return tagMapper.toTagDTO(tagToCreate);
    }

    @Override
    @Transactional
    public void deleteTagById(int tagId) {
        tagRepository.deleteById(tagId);
    }
}
