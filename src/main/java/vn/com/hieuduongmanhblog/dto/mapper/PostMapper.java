package vn.com.hieuduongmanhblog.dto.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import vn.com.hieuduongmanhblog.dto.PostDTO;
import vn.com.hieuduongmanhblog.entity.Post;
import org.springframework.stereotype.Service;
import vn.com.hieuduongmanhblog.entity.Tag;
import vn.com.hieuduongmanhblog.repository.TagRepository;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Mapper class for converting between Post entities and PostDTOs.
 * <p>
 * Handles transformations in both directions: entity → DTO and DTO → entity.
 * Also provides methods to update existing Post entities from DTOs.
 * Tag mapping is handled here, including converting tags to strings and
 * fetching existing Tag entities from the database.
 */
@Component
public class PostMapper {
    private final TagRepository tagRepository;

    @Autowired
    public PostMapper(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public PostDTO toPostDTO(Post post) {
        return new PostDTO(
                post.getId(),
                post.getTitle(),
                post.getDescription(),
                post.getContent(),
                post.getCreatedAt(),
                post.getUpdatedAt(),
                post.getUser().getUsername(),
                mapTags(post.getTags())
        );
    }

    public Post toPost(PostDTO postDTO) {
        Post post = new Post();
        post.setTitle(postDTO.title());
        post.setDescription(postDTO.description());
        post.setContent(postDTO.content());
        post.setCreatedAt(postDTO.createdAt());
        post.setUpdatedAt(postDTO.updatedAt());
        post.setTags(mapTags(postDTO.tags()));

        return post;
    }

    public void updatePostFromDTO(PostDTO postDTO, Post existingPost) {
        if (postDTO.title() != null && !postDTO.title().isEmpty()) {
            existingPost.setTitle(postDTO.title());
        }
        if (postDTO.description() != null && !postDTO.description().isEmpty()) {
            existingPost.setDescription(postDTO.description());
        }
        if (postDTO.content() != null && !postDTO.content().isEmpty()) {
            existingPost.setContent(postDTO.content());
        }
        if (postDTO.tags() != null && !postDTO.tags().isEmpty()) {
            existingPost.setTags(mapTags(postDTO.tags()));
        }

        existingPost.setUpdatedAt(LocalDateTime.now());
    }


    public String mapTags(Set<Tag> tags) {
        return tags
                .stream()
                .map(Tag::getTagName)
                .collect(Collectors.joining(","));
    }

    public Set<Tag> mapTags(String tags) {
        Set<Tag> tagSet = new HashSet<>();
        if (tags == null || tags.isEmpty()) return tagSet;

        // Split, trim, and filter out empty tag names
        Set<String> tagNames = Arrays.stream(tags.split(","))
                .map(String::trim)
                .filter(tag -> !tag.isEmpty())
                .collect(Collectors.toSet());

        // Fetch only existing tags, new tags will be ignored
        List<Tag> existingTags = tagRepository.findAllByTagNameIn(tagNames);
        tagSet.addAll(existingTags);

        return tagSet;
    }
}
