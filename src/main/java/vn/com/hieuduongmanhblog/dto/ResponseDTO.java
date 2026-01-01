package vn.com.hieuduongmanhblog.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Standardized Response Body")
public record ResponseDTO(
    @Schema(description = "HTTP status code", example = "200")
    int status,

    @Schema(description = "response message", example = "Request was successful")
    String message,

    @Schema(description = "creation timestamp", example = "2025-12-22T14:30:00", type = "string", format = "date-time")
    LocalDateTime timestamp,

    @Schema(description = "response data. Can be any object based on the API response", implementation = Object.class)
    Object data
) {
    public ResponseDTO(int status, String message, LocalDateTime timestamp) {
        this(status, message, timestamp, null);
    }
}
