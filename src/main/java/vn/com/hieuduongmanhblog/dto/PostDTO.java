package vn.com.hieuduongmanhblog.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record PostDTO(
    Integer id,

    @NotNull(message = "title cannot be null")
    String title,

    @NotNull(message = "description cannot be null")
    String description,

    @NotNull(message = "content cannot be null")
    String content,

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    LocalDateTime createdAt,

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    LocalDateTime updatedAt,

    String postAuthor,

    @NotNull(message = "tags cannot be null")
    String tags
) {
    public PostDTO(Integer id, String title, String description, String content, String tags) {
        this(id, title, description, content, null, null, null, tags);
    }

    public PostDTO PostDTOWithDefaultId(Integer newId) {
        return new PostDTO(newId, title, description, content, createdAt, updatedAt, postAuthor, tags);
    }
}
