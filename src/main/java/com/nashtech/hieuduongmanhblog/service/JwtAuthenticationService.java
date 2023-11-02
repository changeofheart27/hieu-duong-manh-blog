package com.nashtech.hieuduongmanhblog.service;

import com.nashtech.hieuduongmanhblog.auth.AuthenticationRequest;
import com.nashtech.hieuduongmanhblog.auth.AuthenticationResponse;
import com.nashtech.hieuduongmanhblog.auth.RegisterRequest;
import com.nashtech.hieuduongmanhblog.entity.Role;
import com.nashtech.hieuduongmanhblog.entity.User;
import com.nashtech.hieuduongmanhblog.repository.RoleRepository;
import com.nashtech.hieuduongmanhblog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Set;

@Service
public class JwtAuthenticationService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtilService jwtService;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public JwtAuthenticationService(
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder,
            JwtUtilService jwtService,
            AuthenticationManager authenticationManager
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }
    public AuthenticationResponse register(RegisterRequest request) {
        User newUser = new User();
        newUser.setUsername(request.getUsername());
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));
        newUser.setEmail(request.getEmail());
        newUser.setCreatedAt(LocalDate.now());
        Role defaultUserRole = roleRepository.findByName("ROLE_USER");
        newUser.setRoles(Set.of(defaultUserRole));
        userRepository.save(newUser);

        String jwtToken = jwtService.generateToken(newUser);

        return new AuthenticationResponse(jwtToken);
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        User foundUser =  userRepository
                .findByUsername(request.getUsername())
                .orElseThrow(
                        () -> new UsernameNotFoundException("User with username " + request.getUsername() + " not found!")
                );
        String jwtToken = jwtService.generateToken(foundUser);

        return new AuthenticationResponse(jwtToken);
    }
}
