package vn.com.hieuduongmanhblog.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import vn.com.hieuduongmanhblog.dto.UserAuthenticationRequestDTO;
import vn.com.hieuduongmanhblog.dto.UserRegistrationRequestDTO;
import vn.com.hieuduongmanhblog.dto.ResponseDTO;
import vn.com.hieuduongmanhblog.service.impl.JwtAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/auth")
public class JwtAuthenticationController {
    private final JwtAuthenticationService authenticationService;

    @Autowired
    public JwtAuthenticationController(JwtAuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    public ResponseDTO registerUser(@Valid @RequestBody UserRegistrationRequestDTO request) {
        String jwtToken = authenticationService.registerUser(request);
        return new ResponseDTO(HttpStatus.CREATED, "Register New User Successful!", LocalDateTime.now(), jwtToken);
    }

    @PostMapping("/login")
    public ResponseDTO authenticateUser(@Valid @RequestBody UserAuthenticationRequestDTO request) {
        String jwtToken = authenticationService.authenticateUser(request);
        return new ResponseDTO(HttpStatus.OK, "Login Successful!", LocalDateTime.now(), jwtToken);
    }
}
