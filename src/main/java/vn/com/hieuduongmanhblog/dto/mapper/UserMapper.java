package vn.com.hieuduongmanhblog.dto.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import vn.com.hieuduongmanhblog.dto.UserDTO;
import vn.com.hieuduongmanhblog.entity.Role;
import vn.com.hieuduongmanhblog.entity.RoleName;
import vn.com.hieuduongmanhblog.entity.User;
import org.springframework.stereotype.Service;
import vn.com.hieuduongmanhblog.exception.ResourceNotFoundException;
import vn.com.hieuduongmanhblog.repository.RoleRepository;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Mapper class for converting between User entities and UserDTOs.
 * <p>
 * Handles transformations in both directions: entity → DTO and DTO → entity.
 * Provides methods to update existing User entities from DTOs.
 * Also manages conversion of Role sets to comma-separated strings and vice versa.
 */
@Component
public class UserMapper {
    @Value("${project.image.url}")
    private String avatarUrlBase;

    private final RoleRepository roleRepository;

    @Autowired
    public UserMapper(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public UserDTO toUserDTO(User user) {
        String avatarUrl = user.getAvatar() != null ? this.avatarUrlBase.trim() + user.getAvatar() : null;
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
        user.setUsername(userDTO.username());
        user.setDob(userDTO.dob());
        user.setEmail(userDTO.email());
        user.setCreatedAt(userDTO.createdAt());
        user.setUpdatedAt(LocalDateTime.now());
        user.setAvatar(userDTO.avatar());
        user.setRoles(mapRoles(userDTO.roles()));

        return user;
    }

    public void updateUserFromDTO(UserDTO userDTO, User existingUser) {
        if (userDTO.dob() != null && !userDTO.dob().toString().isEmpty()) {
            existingUser.setDob(userDTO.dob());
        }
        if (userDTO.email() != null && !userDTO.email().isEmpty()) {
            existingUser.setEmail(userDTO.email());
        }
        existingUser.setUpdatedAt(LocalDateTime.now());
    }

    public String mapRoles(Set<Role> roles) {
        return roles
                .stream()
                .map(role -> role.getRoleName().name())
                .collect(Collectors.joining(","));
    }

    public Set<Role> mapRoles(String roleNames) {
        Set<Role> roleSet = new HashSet<>();
        if (roleNames == null || roleNames.isEmpty()) return roleSet;

        // Split, trim, and filter out empty role names
        Set<String> roleNameSet = Arrays.stream(roleNames.split(","))
                .map(String::trim)
                .filter(name -> !name.isEmpty())
                .map(name -> name.replace("ROLE_", "")) // remove ROLE_ prefix
                .collect(Collectors.toSet());

        // Fetch only existing roles, new roles will be ignored
        List<Role> existingRoles = roleRepository.findAllByRoleNameIn(
                roleNameSet.stream()
                        .map(RoleName::valueOf)
                        .collect(Collectors.toSet())
        );

        roleSet.addAll(existingRoles);

        return roleSet;
    }
}
