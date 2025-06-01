package vn.com.hieuduongmanhblog.dto.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import vn.com.hieuduongmanhblog.dto.PostDTO;
import vn.com.hieuduongmanhblog.entity.Post;
import org.springframework.stereotype.Service;
import vn.com.hieuduongmanhblog.entity.Role;
import vn.com.hieuduongmanhblog.entity.Tag;
import vn.com.hieuduongmanhblog.repository.TagRepository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
        post.setTitle(postDTO.getTitle());
        post.setDescription(postDTO.getDescription());
        post.setContent(postDTO.getContent());
        post.setCreatedAt(postDTO.getCreatedAt());
        post.setUpdatedAt(postDTO.getUpdatedAt());
        post.setTags(mapTags(postDTO.getTags()));

        return post;
    }

    public String mapTags(List<Tag> tags) {
        return tags
                .stream()
                .map(Tag::getTagName)
                .collect(Collectors.joining(","));
    }

    public List<Tag> mapTags(String tags) {
        List<Tag> tagList = new ArrayList<>();
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
