package vn.com.hieuduongmanhblog.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import vn.com.hieuduongmanhblog.dto.*;
import vn.com.hieuduongmanhblog.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@Tag(name = "Authentication Controller", description = "Endpoints for User Authentication")
@RestController
@RequestMapping("/api/v1/auth")
public class JwtAuthenticationController {
    private final AuthenticationService authenticationService;

    @Autowired
    public JwtAuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Operation(description = "Login to the system using username and password")
    @PostMapping("/login")
    public ResponseEntity<ResponseDTO> login(@Valid @RequestBody UserAuthenticationRequestDTO request) {
        UserAuthenticationResponseDTO authenticationResponseDTO = authenticationService.login(request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDTO(
                        HttpStatus.OK.value(),
                        "Login Successful",
                        LocalDateTime.now(),
                        authenticationResponseDTO)
                );
    }

    @Operation(description = "Get new access token using existing refresh token")
    @PostMapping("/token")
    public ResponseEntity<ResponseDTO> refreshAccessToken(@Valid @RequestBody RefreshTokenRequestDTO request) {
        UserAuthenticationResponseDTO authenticationResponseDTO = authenticationService.refreshAccessToken(request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDTO(
                        HttpStatus.OK.value(),
                        "Refresh Access Token Successful",
                        LocalDateTime.now(),
                        authenticationResponseDTO)
                );
    }
}
