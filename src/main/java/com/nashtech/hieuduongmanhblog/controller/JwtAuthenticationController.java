package com.nashtech.hieuduongmanhblog.controller;

import com.nashtech.hieuduongmanhblog.auth.AuthenticationRequest;
import com.nashtech.hieuduongmanhblog.auth.AuthenticationResponse;
import com.nashtech.hieuduongmanhblog.auth.RegisterRequest;
import com.nashtech.hieuduongmanhblog.service.JwtAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class JwtAuthenticationController {
    private final JwtAuthenticationService authenticationService;

    @Autowired
    public JwtAuthenticationController(JwtAuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }
}
