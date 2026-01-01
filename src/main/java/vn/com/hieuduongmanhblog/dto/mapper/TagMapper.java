package vn.com.hieuduongmanhblog.dto.mapper;

import org.springframework.stereotype.Component;
import vn.com.hieuduongmanhblog.dto.TagDTO;
import vn.com.hieuduongmanhblog.entity.Tag;

/**
 * Mapper class for converting between Tag entities and TagDTOs.
 * <p>
 * Handles transformations in both directions: entity → DTO and DTO → entity.
 *
 */
@Component
public class TagMapper {
    public TagDTO toTagDTO(Tag tag) {
        return new TagDTO(
                tag.getId(),
                tag.getTagName()
        );
    }

    public Tag toTag(TagDTO tagDTO) {
        Tag tag = new Tag();
        tag.setId(tagDTO.id());
        tag.setTagName(tagDTO.tagName());

        return tag;
    }
}
