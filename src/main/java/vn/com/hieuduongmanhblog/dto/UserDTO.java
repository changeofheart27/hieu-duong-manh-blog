package vn.com.hieuduongmanhblog.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class UserDTO {
    private Integer id;
    @NotBlank(message = "username cannot be blank")
    private String username;
    private LocalDate dob;
    @Pattern(regexp = "^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$", message = "email is not valid")
    private String email;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    @NotBlank(message = "roles cannot be blank")
    private String roles;

    public UserDTO() {

    }

    public UserDTO(Integer id, String username, LocalDate dob, String email, String roles) {
        this.id = id;
        this.username = username;
        this.dob = dob;
        this.email = email;
        this.roles = roles;
    }

    public UserDTO(Integer id, String username, LocalDate dob, String email, LocalDateTime createdAt, LocalDateTime updatedAt, String roles) {
        this.id = id;
        this.username = username;
        this.dob = dob;
        this.email = email;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
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

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }
}
