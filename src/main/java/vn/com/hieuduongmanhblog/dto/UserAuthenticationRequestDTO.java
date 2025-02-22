package vn.com.hieuduongmanhblog.dto;

import jakarta.validation.constraints.NotNull;

public class UserAuthenticationRequestDTO {
    @NotNull(message = "username cannot be null")
    private String username;

    @NotNull(message = "username cannot be null")
    private String password;

    public UserAuthenticationRequestDTO() {

    }

    public UserAuthenticationRequestDTO(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
