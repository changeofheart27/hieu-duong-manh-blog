package com.nashtech.hieuduongmanhblog.dto;

import java.time.LocalDate;

public class PostDTO {
    private Integer id;
    private String title;
    private String description;
    private String content;
    private LocalDate createdAt;
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

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public String getPostAuthor() {
        return postAuthor;
    }

    public void setPostAuthor(String postAuthor) {
        this.postAuthor = postAuthor;
    }
}
