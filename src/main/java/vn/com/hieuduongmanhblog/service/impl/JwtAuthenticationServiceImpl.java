package vn.com.hieuduongmanhblog.service.impl;

import org.springframework.security.core.Authentication;
import vn.com.hieuduongmanhblog.dto.UserAuthenticationRequestDTO;
import vn.com.hieuduongmanhblog.dto.UserRegistrationRequestDTO;
import vn.com.hieuduongmanhblog.entity.Role;
import vn.com.hieuduongmanhblog.entity.RoleName;
import vn.com.hieuduongmanhblog.entity.User;
import vn.com.hieuduongmanhblog.exception.ResourceNotFoundException;
import vn.com.hieuduongmanhblog.repository.RoleRepository;
import vn.com.hieuduongmanhblog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vn.com.hieuduongmanhblog.service.AuthenticationService;
import vn.com.hieuduongmanhblog.service.JwtUtilService;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

@Service
public class JwtAuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtilService jwtService;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public JwtAuthenticationServiceImpl(
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

    public String registerUser(UserRegistrationRequestDTO request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("User with username " + request.getUsername() + " already exists!");
        }

        User newUser = new User(
                request.getUsername(),
                passwordEncoder.encode(request.getPassword()),
                request.getEmail(),
                LocalDateTime.now()
        );
        Role defaultUserRole = roleRepository.findByRoleName(RoleName.USER)
                .orElseThrow(() -> new ResourceNotFoundException("Could not find Role with roleName - " + RoleName.USER.name()));
        newUser.setRoles(Set.of(defaultUserRole));
        userRepository.save(newUser);

        return jwtService.generateToken(newUser.getUsername());
    }

    public String authenticateUser(UserAuthenticationRequestDTO request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        if (authentication.isAuthenticated()) {
            return jwtService.generateToken(request.getUsername());
        } else {
            throw new UsernameNotFoundException("User with username " + request.getUsername() + " not found!");
        }
    }
}
