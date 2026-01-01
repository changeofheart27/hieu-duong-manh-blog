package vn.com.hieuduongmanhblog.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Set;

@Schema(description = "User Registration Response Body")
public record UserAuthenticationResponseDTO(
        @Schema(description = "access token")
        String accessToken,

        @Schema(description = "refresh token")
        String refreshToken,

        @Schema(description = "token type")
        String tokenType,

        @Schema(description = "user ID")
        Integer userId,

        @Schema(description = "username")
        String username,

        @Schema(description = "email address")
        String email,

        @Schema(description = "user roles")
        Set<String>roles
) {
}
