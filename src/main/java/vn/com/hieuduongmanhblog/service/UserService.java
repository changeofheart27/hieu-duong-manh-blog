package vn.com.hieuduongmanhblog.service;

import vn.com.hieuduongmanhblog.dto.UserDTO;

import java.util.List;

public interface UserService {
    List<UserDTO> getAllUsers();

    UserDTO findUserById(int userId);

    UserDTO findUserByUsername(String username);

    UserDTO updateUserById(int userId, UserDTO newUser);

    void deleteUserById(int userId);
}
