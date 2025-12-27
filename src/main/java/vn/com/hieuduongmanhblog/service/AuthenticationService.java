package vn.com.hieuduongmanhblog.service;

import vn.com.hieuduongmanhblog.dto.RefreshTokenRequestDTO;
import vn.com.hieuduongmanhblog.dto.UserAuthenticationRequestDTO;
import vn.com.hieuduongmanhblog.dto.UserAuthenticationResponseDTO;
import vn.com.hieuduongmanhblog.dto.UserRegistrationRequestDTO;

public interface AuthenticationService {
    UserAuthenticationResponseDTO login(UserAuthenticationRequestDTO request);

    UserAuthenticationResponseDTO refreshAccessToken(RefreshTokenRequestDTO request);
}
