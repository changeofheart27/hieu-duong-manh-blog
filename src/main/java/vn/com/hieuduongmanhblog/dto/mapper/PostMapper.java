package vn.com.hieuduongmanhblog.dto.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import vn.com.hieuduongmanhblog.dto.PostDTO;
import vn.com.hieuduongmanhblog.entity.Post;
import org.springframework.stereotype.Service;
import vn.com.hieuduongmanhblog.entity.Tag;
import vn.com.hieuduongmanhblog.repository.TagRepository;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
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
        Set<Tag> tagList = new HashSet<>();
        String[] tagNameArr = tags.split(",");
        for (String tagName : tagNameArr) {
            final String trimmedTagName = tagName.trim();
            Tag tag = tagRepository.findByTagName(trimmedTagName)
                    .orElseGet(() -> tagRepository.save(new Tag(trimmedTagName)));
            tagList.add(tag);
        }
        return tagList;
    }
}
