package com.nashtech.hieuduongmanhblog.service;

import com.nashtech.hieuduongmanhblog.entity.User;
import com.nashtech.hieuduongmanhblog.exception.ResourceNotFoundException;
import com.nashtech.hieuduongmanhblog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User findUserById(int userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            return optionalUser.get();
        } else throw new ResourceNotFoundException("Could not find User with id - " + userId);
    }

    @Override
    public User findUserByUsername(String username) {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isPresent()) {
            return optionalUser.get();
        } else throw new ResourceNotFoundException("Could not find User with username - " + username);
    }

    @Override
    @Transactional
    public User updateUserById(int userId, User newUser) {
        User userToUpdate = findUserById(userId);
        UserDetails currentUserInfo = getCurrentLoggedInUser();
        String currentUserRole = currentUserInfo.getAuthorities()
                .stream()
                .map(role -> role.getAuthority())
                .collect(Collectors.joining(","));
        if (currentUserRole.contains("USER") || currentUserRole.contains("AUTHOR")) {
            // check if user to update has the same username as current user
            if (!currentUserInfo.getUsername().equals(userToUpdate.getUsername())) {
                throw new RuntimeException("Unable to update User with username - " + userToUpdate.getUsername());
            }
        }

        userToUpdate.setDob(newUser.getDob());
        userToUpdate.setEmail(newUser.getEmail());
        userToUpdate.setCreatedAt(LocalDate.now());
        return userRepository.save(userToUpdate);
    }

    @Override
    @Transactional
    public void deleteUserById(int userId) {
        User userToDelete = findUserById(userId);
        UserDetails currentUserInfo = getCurrentLoggedInUser();
        String currentUserRole = currentUserInfo.getAuthorities()
                .stream()
                .map(role -> role.getAuthority())
                .collect(Collectors.joining(","));
        if (currentUserRole.contains("USER") || currentUserRole.contains("AUTHOR")) {
            // check if user to delete has the same username as current user
            if (!currentUserInfo.getUsername().equals(userToDelete.getUsername())) {
                throw new RuntimeException("Unable to delete User with username - " + userToDelete.getUsername());
            }
        }
        userRepository.deleteById(userToDelete.getId());
    }

    @Override
    public UserDetails getCurrentLoggedInUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (UserDetails) auth.getPrincipal();
    }
}
