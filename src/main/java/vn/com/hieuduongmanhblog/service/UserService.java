package vn.com.hieuduongmanhblog.service;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;
import vn.com.hieuduongmanhblog.dto.UserDTO;

import java.io.IOException;

public interface UserService {
    Page<UserDTO> getAllUsers(int pageNumber, int pageSize);

    UserDTO findUserById(int userId);

    UserDTO findUserByUsername(String username);

    UserDTO updateUserById(int userId, UserDTO newUser);

    void deleteUserById(int userId);

    UserDTO changeAvatar(int userId, MultipartFile multipartFile) throws IOException;
}
