package vn.com.hieuduongmanhblog.service.impl;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import vn.com.hieuduongmanhblog.dto.RefreshTokenRequestDTO;
import vn.com.hieuduongmanhblog.dto.UserAuthenticationRequestDTO;
import vn.com.hieuduongmanhblog.dto.UserAuthenticationResponseDTO;
import vn.com.hieuduongmanhblog.entity.RefreshToken;
import vn.com.hieuduongmanhblog.entity.User;
import vn.com.hieuduongmanhblog.exception.ResourceNotFoundException;
import vn.com.hieuduongmanhblog.exception.UserDisabledException;
import vn.com.hieuduongmanhblog.repository.RefreshTokenRepository;
import vn.com.hieuduongmanhblog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import vn.com.hieuduongmanhblog.service.AuthenticationService;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class JwtAuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;

    private final RefreshTokenRepository refreshTokenRepository;

    private final JwtUtilService jwtService;

    private final UserDetailsService userDetailsService;

    private final AuthenticationManager authenticationManager;

    @Autowired
    public JwtAuthenticationServiceImpl(
            UserRepository userRepository,
            RefreshTokenRepository refreshTokenRepository,
            JwtUtilService jwtService, UserDetailsService userDetailsService,
            AuthenticationManager authenticationManager
    ) {
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.authenticationManager = authenticationManager;
    }

    @Override
    @Transactional
    public UserAuthenticationResponseDTO login(UserAuthenticationRequestDTO request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.username(),
                        request.password()
                )
        );
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User foundUser = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User with username " + userDetails.getUsername() + " not found!"));
        if (!foundUser.getEnabled()) {
            throw new UsernameNotFoundException("User with username " + userDetails.getUsername() + " is disabled!");
        }
        Set<String> roles = foundUser.getRoles().stream()
                .map(role -> role.getRoleName().name())
                .collect(Collectors.toSet());

        String accessToken = jwtService.generateAccessToken(userDetails);
        String refreshToken = jwtService.generateRefreshToken(userDetails);

        RefreshToken newRefreshTokenEntity = new RefreshToken(
                foundUser,
                refreshToken,
                LocalDate.ofInstant(
                        jwtService.extractExpiryDate(refreshToken).toInstant(),
                        ZoneId.systemDefault()
                ),
                false
        );
        refreshTokenRepository.save(newRefreshTokenEntity);

        return new UserAuthenticationResponseDTO(
                accessToken,
                refreshToken,
                "Bearer",
                foundUser.getId(),
                foundUser.getUsername(),
                foundUser.getEmail(),
                roles
        );
    }

    @Override
    @Transactional
    public UserAuthenticationResponseDTO refreshAccessToken(RefreshTokenRequestDTO request) {
        RefreshToken existingRefreshToken = refreshTokenRepository.findByToken(request.refreshToken())
                .orElseThrow(() -> new ResourceNotFoundException("Invalid refresh token"));
        if (existingRefreshToken.getRevoked()) {
            throw new RuntimeException("Refresh token has been revoked");
        }
        if (existingRefreshToken.getExpiryDate().isBefore(LocalDate.now())) {
            throw new RuntimeException("Refresh token has expired");
        }

        User user = existingRefreshToken.getUser();
        if (!user.getEnabled()) {
            throw new UserDisabledException("User with username " + user.getUsername() + " is disabled!");
        }
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());

        // Generate new tokens
        String newAccessToken = jwtService.generateAccessToken(userDetails);
        String newRefreshToken = jwtService.generateRefreshToken(userDetails);

        // Revoke old refresh token
        existingRefreshToken.setRevoked(true);
        refreshTokenRepository.save(existingRefreshToken);

        // Save new refresh token
        RefreshToken newRefreshTokenEntity = new RefreshToken(
                user,
                newRefreshToken,
                LocalDate.ofInstant(
                        jwtService.extractExpiryDate(newRefreshToken).toInstant(),
                        ZoneId.systemDefault()
                ),
                false
        );
        refreshTokenRepository.save(newRefreshTokenEntity);

        Set<String> roles = user.getRoles().stream()
                .map(role -> role.getRoleName().name())
                .collect(Collectors.toSet());

        return new UserAuthenticationResponseDTO(
                newAccessToken,
                newRefreshToken,
                "Bearer",
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                roles
        );
    }

}
