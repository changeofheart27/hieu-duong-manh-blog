package vn.com.hieuduongmanhblog.dto;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

public class ResponseDTO extends ResponseEntity<ResponseDTO.Payload> {

    public ResponseDTO(HttpStatusCode status, String message, LocalDateTime timestamp) {
        super(new Payload(status.value(), message, timestamp), status);
    }

    public ResponseDTO(HttpStatusCode status, String message, LocalDateTime timestamp, Object data) {
        super(new Payload(status.value(), message, timestamp, data), status);
    }

    public static class Payload {
        private final int status;
        private final String message;
        private final LocalDateTime timestamp;
        private Object data;

        public Payload(int status, String message, LocalDateTime timestamp) {
            this.status = status;
            this.message = message;
            this.timestamp = timestamp;
        }

        public Payload(int status, String message, LocalDateTime timestamp, Object data) {
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
}
