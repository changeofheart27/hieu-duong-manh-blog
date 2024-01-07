package com.nashtech.hieuduongmanhblog.service;

import com.nashtech.hieuduongmanhblog.dto.UserDTO;
import com.nashtech.hieuduongmanhblog.dto.UserMapper;
import com.nashtech.hieuduongmanhblog.entity.Post;
import com.nashtech.hieuduongmanhblog.entity.User;
import com.nashtech.hieuduongmanhblog.exception.ResourceNotFoundException;
import com.nashtech.hieuduongmanhblog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public List<UserDTO> getAllUsers() {
        List<User> users =  userRepository.findAll();
        return users
                .stream()
                .map(user -> userMapper.toUserDTO(user))
                .collect(Collectors.toList());
    }

    @Override
    public UserDTO findUserById(int userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User foundUser = optionalUser.get();
            return userMapper.toUserDTO(foundUser);
        } else throw new ResourceNotFoundException("Could not find User with id - " + userId);
    }

    @Override
    public UserDTO findUserByUsername(String username) {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isPresent()) {
            User foundUser = optionalUser.get();
            return userMapper.toUserDTO(foundUser);
        } else throw new ResourceNotFoundException("Could not find User with username - " + username);
    }

    @Override
    @Transactional
    public UserDTO updateUserById(int userId, UserDTO newUser) {
        User userToUpdate = userRepository
                .findById(userId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Could not find User with id - " + userId)
                );
        UserDetails currentUserInfo = getCurrentLoggedInUser();
        String currentUserRole = currentUserInfo.getAuthorities()
                .stream()
                .map(role -> role.getAuthority())
                .collect(Collectors.joining(","));
        if (!currentUserRole.contains("ADMIN")) {
            // check if user to update has the same username as current user
            if (!currentUserInfo.getUsername().equals(userToUpdate.getUsername())) {
                throw new RuntimeException("Unable to update User with username - " + userToUpdate.getUsername());
            }
        }

        User updatedUser = userRepository.save(userMapper.updateUserFromUserDTO(newUser, userToUpdate));
        return userMapper.toUserDTO(updatedUser);
    }

    @Override
    @Transactional
    public void deleteUserById(int userId) {
        User userToDelete = userRepository
                .findById(userId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Could not find User with id - " + userId)
                );
        UserDetails currentUserInfo = getCurrentLoggedInUser();
        String currentUserRole = currentUserInfo.getAuthorities()
                .stream()
                .map(role -> role.getAuthority())
                .collect(Collectors.joining(","));
        if (!currentUserRole.contains("ADMIN")) {
            // check if user to delete has the same username as current user
            if (!currentUserInfo.getUsername().equals(userToDelete.getUsername())) {
                throw new RuntimeException("Unable to delete User with username - " + userToDelete.getUsername());
            }
        }
        // remove associated references to current user
        List<Post> postsByUser = userToDelete.getPosts();
        postsByUser.forEach(post -> post.setUser(null));

        userRepository.delete(userToDelete);
    }

    private UserDetails getCurrentLoggedInUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (UserDetails) auth.getPrincipal();
    }
}
