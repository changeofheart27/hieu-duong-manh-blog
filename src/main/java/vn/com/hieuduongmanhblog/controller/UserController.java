package vn.com.hieuduongmanhblog.controller;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;
import vn.com.hieuduongmanhblog.dto.ResponseDTO;
import vn.com.hieuduongmanhblog.dto.UserDTO;
import vn.com.hieuduongmanhblog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(params = {})
    public ResponseDTO getAllUsers(
            @RequestParam(value = "page", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(value = "size", defaultValue = "5", required = false) int pageSize
    ) {
        Page<UserDTO> users = this.userService.getAllUsers(pageNumber, pageSize);
        return new ResponseDTO(HttpStatus.OK, "Get All Users Successful", LocalDateTime.now(), users);
    }

    @GetMapping("/{userId}")
    public ResponseDTO findUserById(@PathVariable int userId) {
        UserDTO givenUser = this.userService.findUserById(userId);
        return new ResponseDTO(HttpStatus.OK, "Get User By ID Successful", LocalDateTime.now(), givenUser);
    }

    @GetMapping(params = {"username"})
    public ResponseDTO findUserByUsername(@RequestParam(name = "username") String username) {
        UserDTO givenUser = this.userService.findUserByUsername(username);
        return new ResponseDTO(HttpStatus.OK, "Get User By username Successful", LocalDateTime.now(), givenUser);
    }

    @PatchMapping("/{userId}")
    public ResponseDTO updateExistingUser(@PathVariable int userId, @Valid @RequestBody UserDTO newUser) {
        UserDTO updatedUser = this.userService.updateUserById(userId, newUser);
        return new ResponseDTO(HttpStatus.OK, "Update User Successful", LocalDateTime.now(), updatedUser);
    }

    @DeleteMapping("/{userId}")
    public ResponseDTO deleteUserById(@PathVariable int userId) {
        this.userService.deleteUserById(userId);
        return new ResponseDTO(HttpStatus.OK, "Delete User Successful", LocalDateTime.now());
    }

    @PatchMapping("/{userId}/change-avatar")
    public ResponseDTO changeAvatar(@PathVariable int userId, @RequestPart MultipartFile imageFile) {
        try {
            UserDTO userWithUpdatedAvatar = this.userService.changeAvatar(userId, imageFile);
            return new ResponseDTO(HttpStatus.OK, "Update User Avatar Successful", LocalDateTime.now(), userWithUpdatedAvatar);
        } catch (IOException e) {
            return new ResponseDTO(HttpStatus.BAD_REQUEST, "Update User Avatar Failed: " + e.getMessage(), LocalDateTime.now());
        }
    }
}
