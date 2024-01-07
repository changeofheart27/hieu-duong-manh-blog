package com.nashtech.hieuduongmanhblog.dto;

import com.nashtech.hieuduongmanhblog.entity.User;
import org.springframework.stereotype.Service;

@Service
public class UserMapper {
    public UserDTO toUserDTO(User user) {
        return new UserDTO(
                user.getId(),
                user.getUsername(),
                user.getDob(),
                user.getEmail(),
                user.getCreatedAt(),
                user.getRoles()
        );
    }

    public User updateUserFromUserDTO(UserDTO userDTO, User existingUser) {
        if (userDTO.getUsername() != null && userDTO.getUsername().length() != 0) {
            existingUser.setUsername(userDTO.getUsername());
        }
        if (userDTO.getDob() != null && userDTO.getDob().toString().length() != 0) {
            existingUser.setDob(userDTO.getDob());
        }
        if (userDTO.getEmail() != null && userDTO.getEmail().length() != 0) {
            existingUser.setEmail(userDTO.getEmail());
        }
        if (userDTO.getRoles() != null && !userDTO.getRoles().isEmpty()) {
            existingUser.setRoles(userDTO.getRoles());
        }

        return existingUser;
    }
}
