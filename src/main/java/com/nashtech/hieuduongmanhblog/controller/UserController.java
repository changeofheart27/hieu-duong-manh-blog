package com.nashtech.hieuduongmanhblog.controller;

import com.nashtech.hieuduongmanhblog.dto.UserDTO;
import com.nashtech.hieuduongmanhblog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/users", params = {})
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<UserDTO> findUserById(@PathVariable int userId) {
        UserDTO givenUser = userService.findUserById(userId);
        return new ResponseEntity<>(givenUser, HttpStatus.OK);
    }

    @GetMapping(value = "/users", params = {"username"})
    public ResponseEntity<UserDTO> findUserByUsername(@RequestParam(name = "username") String username) {
        UserDTO givenUser = userService.findUserByUsername(username);
        return new ResponseEntity<>(givenUser, HttpStatus.OK);
    }

    @PutMapping("/users/{userId}")
    public ResponseEntity<UserDTO> updateExistingUser(@PathVariable int userId, @RequestBody UserDTO newUser) {
        UserDTO updatedUser = userService.updateUserById(userId, newUser);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<Void> deleteUserById(@PathVariable int userId) {
        userService.deleteUserById(userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
