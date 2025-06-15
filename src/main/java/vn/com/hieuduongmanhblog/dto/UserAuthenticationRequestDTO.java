package vn.com.hieuduongmanhblog.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Login request containing user credentials")
public class UserAuthenticationRequestDTO {
    @Schema(description = "Username", example = "john_doe", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "username cannot be null")
    private String username;

    @Schema(description = "Password", example = "P@ssw0rd123", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "password cannot be null")
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
