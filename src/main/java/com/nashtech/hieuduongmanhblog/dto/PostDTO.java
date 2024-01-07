package com.nashtech.hieuduongmanhblog.dto;

import com.nashtech.hieuduongmanhblog.entity.User;

import java.time.LocalDate;

public class PostDTO {
    private Integer id;
    private String title;
    private String description;
    private String content;
    private LocalDate createdAt;
    private UserDTO userDTO;

    public PostDTO() {

    }

    public PostDTO(Integer id, String title, String description, String content, LocalDate createdAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.content = content;
        this.createdAt = createdAt;
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

    public UserDTO getUserDTO() {
        return userDTO;
    }

    public void setUserDTO(UserDTO userDTO) {
        this.userDTO = userDTO;
    }
}
