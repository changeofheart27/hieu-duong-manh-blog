package vn.com.hieuduongmanhblog.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDateTime;

@Schema(description = "Registration request with user credentials")
public record UserRegistrationRequestDTO(
    @Schema(description = "Username", example = "john_doe", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "username cannot be null")
    String username,

    @Schema(description = "Password", example = "SecurePass123!", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "password cannot be null")
    String password,

    @Schema(description = "Email address", example = "johndoe@example.com")
    @Pattern(regexp = "^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$", message = "email is not valid")
    String email,

    @Schema(description = "Creation timestamp", example = "2024-01-01T09:30:00")
    LocalDateTime createdAt
) {}
