package vn.com.hieuduongmanhblog.controller;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ResponseDTO> getAllUsers(
            @RequestParam(value = "page", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(value = "size", defaultValue = "5", required = false) int pageSize
    ) {
        Page<UserDTO> users = this.userService.getAllUsers(pageNumber, pageSize);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDTO(HttpStatus.OK.value(), "Get All Users Successful", LocalDateTime.now(), users));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ResponseDTO> findUserById(@PathVariable int userId) {
        UserDTO givenUser = this.userService.findUserById(userId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDTO(HttpStatus.OK.value(), "Get User By ID Successful", LocalDateTime.now(), givenUser));
    }

    @GetMapping(params = {"username"})
    public ResponseEntity<ResponseDTO> findUserByUsername(@RequestParam(name = "username") String username) {
        UserDTO givenUser = this.userService.findUserByUsername(username);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDTO(HttpStatus.OK.value(), "Get User By username Successful", LocalDateTime.now(), givenUser));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<ResponseDTO> updateExistingUser(@PathVariable int userId, @Valid @RequestBody UserDTO newUser) {
        UserDTO updatedUser = this.userService.updateUserById(userId, newUser);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDTO(HttpStatus.OK.value(), "Update User Successful", LocalDateTime.now(), updatedUser));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<ResponseDTO> deleteUserById(@PathVariable int userId) {
        this.userService.deleteUserById(userId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDTO(HttpStatus.OK.value(), "Delete User Successful", LocalDateTime.now()));
    }

    @PatchMapping("/{userId}/change-avatar")
    public ResponseEntity<ResponseDTO> changeAvatar(@PathVariable int userId, @RequestPart MultipartFile imageFile) {
        try {
            UserDTO userWithUpdatedAvatar = this.userService.changeAvatar(userId, imageFile);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDTO(HttpStatus.OK.value(), "Update User Avatar Successful", LocalDateTime.now(), userWithUpdatedAvatar));
        } catch (IOException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseDTO(HttpStatus.BAD_REQUEST.value(), "Update User Avatar Failed: " + e.getMessage(), LocalDateTime.now()));
        }
    }
}
