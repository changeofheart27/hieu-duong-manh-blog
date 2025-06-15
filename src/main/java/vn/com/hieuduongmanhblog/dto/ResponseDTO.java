package vn.com.hieuduongmanhblog.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

@Schema(description = "Standardized response message")
public class ResponseDTO {
    @Schema(description = "HTTP status code", example = "200")
    private final int status;

    @Schema(description = "Response message", example = "Get All Users Successful")
    private final String message;

    @Schema(description = "Creation timestamp", example = "2024-01-01T12:00:00")
    private final LocalDateTime timestamp;

    @Schema(description = "Response data")
    private Object data;

    public ResponseDTO(int status, String message, LocalDateTime timestamp) {
        this.status = status;
        this.message = message;
        this.timestamp = timestamp;
    }

    public ResponseDTO(int status, String message, LocalDateTime timestamp, Object data) {
        this.status = status;
        this.message = message;
        this.timestamp = timestamp;
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public Object getData() {
        return data;
    }
}
