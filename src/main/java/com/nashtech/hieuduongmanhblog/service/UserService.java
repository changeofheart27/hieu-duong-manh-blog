package com.nashtech.hieuduongmanhblog.service;

import com.nashtech.hieuduongmanhblog.entity.User;

import java.util.List;

public interface UserService {
    List<User> getAllUsers();

    User findUserById(int userId);

    User findUserByUsername(String username);

    User updateUserById(int userId, User newUser);

    void deleteUserById(int userId);
}
