package vn.com.hieuduongmanhblog.dto.mapper;

import org.springframework.beans.factory.annotation.Value;
import vn.com.hieuduongmanhblog.dto.UserDTO;
import vn.com.hieuduongmanhblog.entity.Role;
import vn.com.hieuduongmanhblog.entity.User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserMapper {
    @Value("${project.image.url}")
    private String avatarUrlBase;

    public UserDTO toUserDTO(User user) {
        String avatarUrl = this.avatarUrlBase + user.getAvatar();
        return new UserDTO(
                user.getUsername(),
                user.getDob(),
                user.getEmail(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                user.getAvatar(),
                avatarUrl,
                mapRoles(user.getRoles())
        );
    }

    public User toUserEntity(UserDTO userDTO) {
        User user = new User();
        user.setUsername(user.getUsername());
        user.setDob(userDTO.getDob());
        user.setEmail(userDTO.getEmail());
        user.setCreatedAt(userDTO.getCreatedAt());
        user.setUpdatedAt(LocalDateTime.now());
        user.setAvatar(userDTO.getAvatar());
        user.setRoles(mapRoles(userDTO.getRoles()));

        return user;
    }

    public String mapRoles(Set<Role> roles) {
        return roles
                .stream()
                .map(Role::getRoleName)
                .collect(Collectors.joining(","));
    }

    public Set<Role> mapRoles(String roleNames) {
        Set<Role> roleSet = new HashSet<>();
        String[] roleNameArr = roleNames.split(",");
        for (String roleName : roleNameArr) {
            Role role = new Role(roleName);
            roleSet.add(role);
        }

        return roleSet;
    }
}
