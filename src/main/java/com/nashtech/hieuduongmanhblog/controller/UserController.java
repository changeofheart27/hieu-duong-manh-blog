package com.nashtech.hieuduongmanhblog.controller;

import com.nashtech.hieuduongmanhblog.entity.User;
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
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<User> findUserById(@PathVariable int userId) {
        User givenUser = userService.findUserById(userId);
        return new ResponseEntity<>(givenUser, HttpStatus.OK);
    }

    @GetMapping(value = "/users", params = {"username"})
    public ResponseEntity<User> findUserByUsername(@RequestParam(name = "username") String username) {
        User givenUser = userService.findUserByUsername(username);
        return new ResponseEntity<>(givenUser, HttpStatus.OK);
    }

    @PutMapping("/users/{userId}")
    public ResponseEntity<User> updateExistingUser(@PathVariable int userId, @RequestBody User newUser) {
        User updatedUser = userService.updateUserById(userId, newUser);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<Void> deleteUserById(@PathVariable int userId) {
        userService.deleteUserById(userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
