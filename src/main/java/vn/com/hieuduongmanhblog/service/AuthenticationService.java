package vn.com.hieuduongmanhblog.service;

import vn.com.hieuduongmanhblog.dto.UserAuthenticationRequestDTO;
import vn.com.hieuduongmanhblog.dto.UserRegistrationRequestDTO;

public interface AuthenticationService {
    String registerUser(UserRegistrationRequestDTO request);

    String authenticateUser(UserAuthenticationRequestDTO request);
}
