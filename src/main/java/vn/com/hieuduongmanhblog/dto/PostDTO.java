package vn.com.hieuduongmanhblog.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class PostDTO {
    private Integer id;

    @NotNull(message = "title cannot be null")
    private String title;

    @NotNull(message = "description cannot be null")
    private String description;

    @NotNull(message = "content cannot be null")
    private String content;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private String postAuthor;

    @NotNull(message = "tags cannot be null")
    private String tags;

    public PostDTO() {

    }

    public PostDTO(Integer id, String title, String description, String content, String postAuthor) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.content = content;
        this.postAuthor = postAuthor;
    }

    public PostDTO(Integer id, String title, String description, String content, LocalDateTime createdAt, LocalDateTime updatedAt, String postAuthor, String tags) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.postAuthor = postAuthor;
        this.tags = tags;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getPostAuthor() {
        return postAuthor;
    }

    public void setPostAuthor(String postAuthor) {
        this.postAuthor = postAuthor;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }
}
