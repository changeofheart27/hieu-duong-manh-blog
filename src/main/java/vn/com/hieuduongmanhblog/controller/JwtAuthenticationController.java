package vn.com.hieuduongmanhblog.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import vn.com.hieuduongmanhblog.dto.UserAuthenticationRequestDTO;
import vn.com.hieuduongmanhblog.dto.UserRegistrationRequestDTO;
import vn.com.hieuduongmanhblog.dto.ResponseDTO;
import vn.com.hieuduongmanhblog.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@Tag(name = "Authentication Controller", description = "Endpoints for user authentication")
@RestController
@RequestMapping("/api/v1/auth")
public class JwtAuthenticationController {
    private final AuthenticationService authenticationService;

    @Autowired
    public JwtAuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Operation(
            summary = "User Registration",
            description = "Register new user on the system using username and password."
    )
    @PostMapping("/register")
    public ResponseEntity<ResponseDTO> registerUser(@Valid @RequestBody UserRegistrationRequestDTO request) {
        String jwtToken = authenticationService.registerUser(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDTO(HttpStatus.CREATED.value(), "Register New User Successful", LocalDateTime.now(), jwtToken));
    }

    @Operation(
            summary = "User Authentication",
            description = "Login the system using username and password."
    )
    @PostMapping("/login")
    public ResponseEntity<ResponseDTO> authenticateUser(@Valid @RequestBody UserAuthenticationRequestDTO request) {
        String jwtToken = authenticationService.authenticateUser(request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDTO(HttpStatus.OK.value(), "Login Successful", LocalDateTime.now(), jwtToken));
    }
}
