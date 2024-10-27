package vn.com.hieuduongmanhblog.dto.mapper;

import vn.com.hieuduongmanhblog.dto.UserDTO;
import vn.com.hieuduongmanhblog.entity.Role;
import vn.com.hieuduongmanhblog.entity.User;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserMapper {
    public UserDTO toUserDTO(User user) {
        return new UserDTO(
                user.getId(),
                user.getUsername(),
                user.getDob(),
                user.getEmail(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                mapRoles(user.getRoles())
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

        return existingUser;
    }

    public String mapRoles(Set<Role> roles) {
        return roles.stream().map(role -> role.getRoleName()).collect(Collectors.joining(","));
    }
}
