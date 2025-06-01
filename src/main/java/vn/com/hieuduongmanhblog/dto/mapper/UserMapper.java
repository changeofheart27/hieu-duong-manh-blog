package vn.com.hieuduongmanhblog.dto.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import vn.com.hieuduongmanhblog.dto.UserDTO;
import vn.com.hieuduongmanhblog.entity.Role;
import vn.com.hieuduongmanhblog.entity.RoleName;
import vn.com.hieuduongmanhblog.entity.User;
import org.springframework.stereotype.Service;
import vn.com.hieuduongmanhblog.exception.ResourceNotFoundException;
import vn.com.hieuduongmanhblog.repository.RoleRepository;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserMapper {
    @Value("${project.image.url}")
    private String avatarUrlBase;

    private final RoleRepository roleRepository;

    @Autowired
    public UserMapper(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public UserDTO toUserDTO(User user) {
        String avatarUrl = user.getAvatar() != null ? this.avatarUrlBase + user.getAvatar() : null;
        return new UserDTO(
                user.getId(),
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
                .map(role -> role.getRoleName().name())
                .collect(Collectors.joining(","));
    }

    public Set<Role> mapRoles(String roleNames) {
        Set<Role> roleSet = new HashSet<>();
        String[] roleNameArr = roleNames.split(",");
        for (String roleName : roleNameArr) {
            final String trimmedRoleName = roleName.trim();
            Role role = roleRepository
                    .findByRoleName(RoleName.valueOf(roleName.replace("ROLE_", "")))
                    .orElseThrow(() -> new ResourceNotFoundException("Could not find Role with roleName - " + trimmedRoleName));
            roleSet.add(role);
        }

        return roleSet;
    }
}
