package vn.com.hieuduongmanhblog.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Login request containing user credentials")
public record UserAuthenticationRequestDTO(
    @Schema(description = "Username", example = "john_doe", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "username cannot be null")
    String username,

    @Schema(description = "Password", example = "P@ssw0rd123", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "password cannot be null")
    String password
) {}
