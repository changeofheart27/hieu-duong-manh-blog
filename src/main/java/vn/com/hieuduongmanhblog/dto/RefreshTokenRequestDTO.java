package vn.com.hieuduongmanhblog.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Refresh Token Request Payload")
public record RefreshTokenRequestDTO(
        @Schema(description = "refresh token", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Refresh Token is required")
        String refreshToken
) {
}
