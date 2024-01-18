package com.nashtech.hieuduongmanhblog.dto;

import java.time.LocalDate;

public class UserDTO {
    private Integer id;
    private String username;
    private LocalDate dob;
    private String email;
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

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }
}
