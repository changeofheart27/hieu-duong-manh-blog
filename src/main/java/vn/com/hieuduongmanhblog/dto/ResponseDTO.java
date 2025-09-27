package vn.com.hieuduongmanhblog.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Standardized response message")
public record ResponseDTO(
    @Schema(description = "HTTP status code", example = "200")
    int status,

    @Schema(description = "Response message", example = "Get All Users Successful")
    String message,

    @Schema(description = "Creation timestamp", example = "2024-01-01T12:00:00")
    LocalDateTime timestamp,

    @Schema(description = "Response data")
    Object data
) {
    public ResponseDTO(int status, String message, LocalDateTime timestamp) {
        this(status, message, timestamp, null);
    }
}
