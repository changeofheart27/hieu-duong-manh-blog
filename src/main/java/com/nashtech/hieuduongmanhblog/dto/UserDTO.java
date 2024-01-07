package com.nashtech.hieuduongmanhblog.dto;

import com.nashtech.hieuduongmanhblog.entity.Role;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Set;

public class UserDTO {
    private Integer id;
    private String username;
    private LocalDate dob;
    private String email;
    private LocalDate createdAt;
    private Set<Role> roles;

    public UserDTO() {

    }

    public UserDTO(Integer id, String username, LocalDate dob, String email, LocalDate createdAt, Set<Role> roles) {
        this.id = id;
        this.username = username;
        this.dob = dob;
        this.email = email;
        this.createdAt = createdAt;
        this.roles = roles;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}
