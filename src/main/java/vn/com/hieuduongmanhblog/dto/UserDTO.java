package vn.com.hieuduongmanhblog.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record UserDTO(
    Integer id,

    String username,

    @NotNull(message = "dob cannot be null")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate dob,

    @NotNull(message = "email cannot be null")
    @Pattern(regexp = "^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$", message = "email is not valid")
    String email,

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    LocalDateTime createdAt,

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    LocalDateTime updatedAt,

    String avatar,

    String avatarUrl,

    String roles
) {
    public UserDTO(Integer id, String username, LocalDate dob, String email, String roles) {
        this(id, username, dob, email, null, null, null, null, roles);
    }

    public UserDTO UserDTOWithDefaultId(Integer newId) {
        return new UserDTO(newId, username, dob, email, createdAt, updatedAt, avatar, avatarUrl, roles);
    }

    public UserDTO UserDTOWithAvatarUrl(String newAvatarUrl) {
        return new UserDTO(id, username, dob, email, createdAt, updatedAt, avatar, newAvatarUrl, roles);
    }
}
