package vn.com.hieuduongmanhblog.dto;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

public class PostDTO {
    private Integer id;
    @NotBlank(message = "title cannot be blank")
    private String title;
    @NotBlank(message = "description cannot be blank")
    private String description;
    @NotBlank(message = "content cannot be blank")
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    @NotBlank(message = "author cannot be blank")
    private String postAuthor;

    public PostDTO() {

    }

    public PostDTO(Integer id, String title, String description, String content, String postAuthor) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.content = content;
        this.postAuthor = postAuthor;
    }

    public PostDTO(Integer id, String title, String description, String content, LocalDateTime createdAt, LocalDateTime updatedAt, String postAuthor) {
            this.id = id;
            this.title = title;
            this.description = description;
            this.content = content;
            this.createdAt = createdAt;
            this.updatedAt = updatedAt;
            this.postAuthor = postAuthor;
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
}
