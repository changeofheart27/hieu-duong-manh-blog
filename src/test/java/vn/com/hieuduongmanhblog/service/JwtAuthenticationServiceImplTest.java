package vn.com.hieuduongmanhblog.service;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import vn.com.hieuduongmanhblog.dto.UserAuthenticationRequestDTO;
import vn.com.hieuduongmanhblog.dto.UserRegistrationRequestDTO;
import vn.com.hieuduongmanhblog.entity.Role;
import vn.com.hieuduongmanhblog.entity.RoleName;
import vn.com.hieuduongmanhblog.entity.User;
import vn.com.hieuduongmanhblog.exception.UserAlreadyExistAuthenticationException;
import vn.com.hieuduongmanhblog.repository.RoleRepository;
import vn.com.hieuduongmanhblog.repository.UserRepository;
import vn.com.hieuduongmanhblog.service.impl.JwtAuthenticationServiceImpl;

import java.time.LocalDateTime;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class JwtAuthenticationServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtilService jwtUtilService;

    @InjectMocks
    private JwtAuthenticationServiceImpl jwtAuthenticationService;

    private String jwtToken;

    private Role userRole;

    @BeforeEach
    void setUp() {
        userRole = new Role(1, RoleName.USER);
        jwtToken = "token";
    }

    @AfterEach
    void tearDown() {
        // do nothing
    }

    @Test
    @DisplayName("Register New User Should Return JWT Token")
    void testRegisterUser() {
        UserRegistrationRequestDTO registrationRequestDTO =
                new UserRegistrationRequestDTO("username123", "password123", "username@email.com", LocalDateTime.now());
        User createdUser = new User(
                registrationRequestDTO.getUsername(),
                registrationRequestDTO.getPassword(),
                registrationRequestDTO.getEmail(),
                registrationRequestDTO.getCreatedAt()
        );

        // mock the repository behavior
        Mockito.when(userRepository.existsByUsername("username123")).thenReturn(false);
        Mockito.when(userRepository.save(ArgumentMatchers.any(User.class))).thenReturn(createdUser);
        Mockito.when(roleRepository.findByRoleName(RoleName.USER)).thenReturn(Optional.of(userRole));
        Mockito.when(jwtUtilService.generateToken(registrationRequestDTO.getUsername())).thenReturn(jwtToken);

        // call the service method
        String actualJwtToken = jwtAuthenticationService.registerUser(registrationRequestDTO);

        Assertions.assertNotNull(actualJwtToken);
        Assertions.assertEquals(actualJwtToken, jwtToken, "Invalid token");

        Mockito.verify(this.userRepository, Mockito.times(1)).existsByUsername(ArgumentMatchers.anyString());
        Mockito.verify(this.userRepository, Mockito.times(1)).save(ArgumentMatchers.any(User.class));
    }

    @Test
    @DisplayName("Register New User With Existing username Should Throw Exception")
    void testRegisterUserFailed() {
        UserRegistrationRequestDTO registrationRequestDTO =
                new UserRegistrationRequestDTO("hieudm", "test@123", "hieudm@email.com", LocalDateTime.now());

        Mockito.when(userRepository.existsByUsername(registrationRequestDTO.getUsername())).thenThrow(
                new UserAlreadyExistAuthenticationException("User with username " + registrationRequestDTO.getUsername() + " already exists!")
        );

        Assertions.assertThrows(
                UserAlreadyExistAuthenticationException.class,
                () -> jwtAuthenticationService.registerUser(registrationRequestDTO),
                "Should throw exception"
        );

        Mockito.verify(this.userRepository, Mockito.times(1)).existsByUsername(ArgumentMatchers.anyString());
        Mockito.verify(this.userRepository, Mockito.times(0)).save(ArgumentMatchers.any(User.class));
    }

    @Test
    @DisplayName("Authenticate Existing User Should Return JWT Token")
    void testAuthenticateUserSuccess() {
        UserAuthenticationRequestDTO authenticationRequestDTO = new UserAuthenticationRequestDTO("hieudm", "test@123");
        UsernamePasswordAuthenticationToken newUser = new UsernamePasswordAuthenticationToken(
                authenticationRequestDTO.getUsername(),
                authenticationRequestDTO.getPassword()
        );
        // Spring Security: mock SecurityContext object and related entities
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Authentication auth = Mockito.mock(Authentication.class);

        Mockito.when(authenticationManager.authenticate(newUser)).thenReturn(auth);
        Mockito.when(auth.isAuthenticated()).thenReturn(true);
        Mockito.when(jwtUtilService.generateToken(authenticationRequestDTO.getUsername())).thenReturn(jwtToken);

        String actualJwtToken = jwtAuthenticationService.authenticateUser(authenticationRequestDTO);

        Assertions.assertNotNull(actualJwtToken);
        Assertions.assertEquals(actualJwtToken, jwtToken, "Invalid token");

        Mockito.verify(authenticationManager, Mockito.times(1)).authenticate(newUser);
    }

    @Test
    @DisplayName("Authenticate Not Existing User Should Throw Exception")
    void testAuthenticateUserFailed() {
        UserAuthenticationRequestDTO authenticationRequestDTO = new UserAuthenticationRequestDTO("newusername", "password123");
        UsernamePasswordAuthenticationToken newUser = new UsernamePasswordAuthenticationToken(
                authenticationRequestDTO.getUsername(),
                authenticationRequestDTO.getPassword()
        );
        // Spring Security: mock SecurityContext object and related entities
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Authentication auth = Mockito.mock(Authentication.class);

        Mockito.when(authenticationManager.authenticate(newUser)).thenReturn(auth);
        Mockito.when(auth.isAuthenticated()).thenReturn(false);

        Assertions.assertThrows(
                UsernameNotFoundException.class,
                () -> jwtAuthenticationService.authenticateUser(authenticationRequestDTO),
                "Should throw exception"
        );

        Mockito.verify(authenticationManager, Mockito.times(1)).authenticate(newUser);
    }

}
