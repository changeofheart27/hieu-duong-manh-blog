package vn.com.hieuduongmanhblog.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Standardized Response Body")
public record ResponseDTO(
    @Schema(description = "HTTP status code")
    int status,

    @Schema(description = "response message")
    String message,

    @Schema(description = "creation timestamp")
    LocalDateTime timestamp,

    @Schema(description = "response data")
    Object data
) {
    public ResponseDTO(int status, String message, LocalDateTime timestamp) {
        this(status, message, timestamp, null);
    }
}
