package vn.com.hieuduongmanhblog.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDateTime;

@Schema(description = "Registration request with user credentials")
public class UserRegistrationRequestDTO {
    @Schema(description = "Username", example = "john_doe", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "username cannot be null")
    private String username;

    @Schema(description = "Password", example = "SecurePass123!", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "password cannot be null")
    private String password;

    @Schema(description = "Email address", example = "johndoe@example.com")
    @Pattern(regexp = "^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$", message = "email is not valid")
    private String email;

    @Schema(description = "Creation timestamp", example = "2024-01-01T09:30:00")
    private LocalDateTime createdAt;

    public UserRegistrationRequestDTO() {

    }

    public UserRegistrationRequestDTO(String username, String password, String email, LocalDateTime createdAt) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.createdAt = createdAt;
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
}
