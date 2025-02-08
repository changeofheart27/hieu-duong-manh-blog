package vn.com.hieuduongmanhblog.controller;

import vn.com.hieuduongmanhblog.dto.ResponseDTO;
import vn.com.hieuduongmanhblog.dto.UserDTO;
import vn.com.hieuduongmanhblog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/users")
    public ResponseDTO getAllUsers() {
        List<UserDTO> users = userService.getAllUsers();
        return new ResponseDTO(HttpStatus.OK, "Get All Users Successful", LocalDateTime.now(), users);
    }

    @GetMapping("/users/{userId}")
    public ResponseDTO findUserById(@PathVariable int userId) {
        UserDTO givenUser = userService.findUserById(userId);
        return new ResponseDTO(HttpStatus.OK, "Get User By ID Successful", LocalDateTime.now(), givenUser);
    }

    @GetMapping(value = "/users", params = {"username"})
    public ResponseDTO findUserByUsername(@RequestParam(name = "username") String username) {
        UserDTO givenUser = userService.findUserByUsername(username);
        return new ResponseDTO(HttpStatus.OK, "Get User By username Successful", LocalDateTime.now(), givenUser);
    }

    @PutMapping("/users/{userId}")
    public ResponseDTO updateExistingUser(@PathVariable int userId, @RequestBody UserDTO newUser) {
        UserDTO updatedUser = userService.updateUserById(userId, newUser);
        return new ResponseDTO(HttpStatus.OK, "Update User Successful", LocalDateTime.now(), updatedUser);
    }

    @DeleteMapping("/users/{userId}")
    public ResponseDTO deleteUserById(@PathVariable int userId) {
        userService.deleteUserById(userId);
        return new ResponseDTO(HttpStatus.OK, "Delete User Successful", LocalDateTime.now());
    }
}
