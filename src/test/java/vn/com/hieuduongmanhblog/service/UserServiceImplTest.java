package vn.com.hieuduongmanhblog.service;

import vn.com.hieuduongmanhblog.dto.UserDTO;
import vn.com.hieuduongmanhblog.dto.mapper.UserMapper;
import vn.com.hieuduongmanhblog.entity.Post;
import vn.com.hieuduongmanhblog.entity.Role;
import vn.com.hieuduongmanhblog.entity.User;
import vn.com.hieuduongmanhblog.exception.ResourceNotFoundException;
import vn.com.hieuduongmanhblog.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import vn.com.hieuduongmanhblog.service.impl.UserServiceImpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userServiceImpl;

    private List<User> users;

    @BeforeEach
    void setUp() {
        Role userRole = new Role();
        userRole.setRoleName("ROLE_USER");
        Role authorRole = new Role();
        userRole.setRoleName("ROLE_AUTHOR");
        Role adminRole = new Role();
        userRole.setRoleName("ROLE_ADMIN");
        // prepare data to test
        User user1 = new User(1, "username1", "password1", null, "username1@email.com", LocalDateTime.now(), LocalDateTime.now(), Set.of(userRole, authorRole, adminRole));
        User user2 = new User(2, "username2", "password2", null, "username2@email.com", LocalDateTime.now(), LocalDateTime.now(), Set.of(userRole, authorRole));
        User user3 = new User(3, "username3", "password3", null, "username3@email.com", LocalDateTime.now(), LocalDateTime.now(), Set.of(userRole, authorRole));
        User user4 = new User(4, "username4", "password4", null, "username4@email.com", LocalDateTime.now(), LocalDateTime.now(), Set.of(userRole));

        users = new ArrayList<>();
        users.add(user1);
        users.add(user2);
        users.add(user3);
        users.add(user4);
    }

    @AfterEach
    void tearDown() {
        // do nothing
    }

    @Test
    @DisplayName("Get All Users Should Return List Of Users")
    void testGetAllUsers() {
        UserDTO mockUserDTO = Mockito.mock(UserDTO.class);
        Mockito.when(userRepository.findAll()).thenReturn(this.users);
        Mockito.when(userMapper.toUserDTO(ArgumentMatchers.any(User.class))).thenReturn(mockUserDTO);

        List<UserDTO> actualUserDTOs = userServiceImpl.getAllUsers();

        Assertions.assertEquals(actualUserDTOs.size(), this.users.size(), "Size should be 4");

        Mockito.verify(this.userRepository, Mockito.times(1)).findAll();
        Mockito.verify(this.userMapper, Mockito.times(4)).toUserDTO(ArgumentMatchers.any(User.class));
    }

    @Test
    @DisplayName("Find User By Id Should Return A Valid User")
    void testFindUserByIdSuccess() {
        UserDTO userDTO = new UserDTO(1, "username1", null, "username1@email.com", "ROLE_ADMIN,ROLE_AUTHOR,ROLE_USER");
        Mockito.when(userRepository.findById(1)).thenReturn(Optional.of(users.get(0)));
        Mockito.when(userMapper.toUserDTO(ArgumentMatchers.any(User.class))).thenReturn(userDTO);

        UserDTO actualUserDTO = userServiceImpl.findUserById(1);

        Assertions.assertEquals(actualUserDTO.getId(), this.users.get(0).getId(), "Id should match each other");
        Assertions.assertEquals(actualUserDTO.getUsername(), this.users.get(0).getUsername(), "Username should match each other");
        Assertions.assertEquals(actualUserDTO.getDob(), this.users.get(0).getDob(), "Dob should match each other");
        Assertions.assertEquals(actualUserDTO.getEmail(), this.users.get(0).getEmail(), "Email should match each other");

        Mockito.verify(this.userRepository, Mockito.times(1)).findById(1);
        Mockito.verify(this.userMapper, Mockito.times(1)).toUserDTO(ArgumentMatchers.any(User.class));
    }

    @Test
    @DisplayName("Find User By Id Should Throw Exception")
    void testFindUserByIdFailed() {
        Mockito.when(userRepository.findById(10)).thenThrow(new ResourceNotFoundException("Could not find User with id - 10"));

        Assertions.assertThrows(
                ResourceNotFoundException.class,
                () -> { userServiceImpl.findUserById(10); },
                "Should throw exception"
        );

        Mockito.verify(this.userRepository, Mockito.times(1)).findById(10);
        Mockito.verify(this.userMapper, Mockito.times(0)).toUserDTO(ArgumentMatchers.any(User.class));
    }

    @Test
    @DisplayName("Find User By username Should Return A Valid User")
    void testFindUserByUsernameSuccess() {
        UserDTO userDTO = new UserDTO(2, "username2", null, "username2@email.com", "ROLE_ADMIN,ROLE_AUTHOR");
        Mockito.when(userRepository.findByUsername("username2")).thenReturn(Optional.of(users.get(1)));
        Mockito.when(userMapper.toUserDTO(ArgumentMatchers.any(User.class))).thenReturn(userDTO);

        UserDTO actualUserDTO = userServiceImpl.findUserByUsername("username2");

        Assertions.assertEquals(actualUserDTO.getId(), this.users.get(1).getId(), "Id should match each other");
        Assertions.assertEquals(actualUserDTO.getUsername(), this.users.get(1).getUsername(), "Username should match each other");
        Assertions.assertEquals(actualUserDTO.getDob(), this.users.get(1).getDob(), "Dob should match each other");
        Assertions.assertEquals(actualUserDTO.getEmail(), this.users.get(1).getEmail(), "Email should match each other");

        Mockito.verify(this.userRepository, Mockito.times(1)).findByUsername("username2");
        Mockito.verify(this.userMapper, Mockito.times(1)).toUserDTO(ArgumentMatchers.any(User.class));
    }

    @Test
    @DisplayName("Find User By username Should Throw Exception")
    void testFindUserByUsernameFailed() {
        Mockito.when(userRepository.findByUsername("username123")).thenThrow(new ResourceNotFoundException("Could not find User with username - username123"));

        Assertions.assertThrows(
                ResourceNotFoundException.class,
                () -> { userServiceImpl.findUserByUsername("username123"); },
                "Should throw exception"
        );

        Mockito.verify(this.userRepository, Mockito.times(1)).findByUsername("username123");
        Mockito.verify(this.userMapper, Mockito.times(0)).toUserDTO(ArgumentMatchers.any(User.class));
    }

    @Test
    @DisplayName("Update Existing User Should Return Updated User")
    void testUpdateUserSuccess() {
        // Spring Security: mock SecurityContext object and related entities
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Authentication auth = Mockito.mock(Authentication.class);
        UserDetails userDetails = Mockito.mock(UserDetails.class);
        UserDTO userDTO = new UserDTO();
        userDTO.setDob(LocalDate.of(1999, 7, 2));
        userDTO.setEmail("newusername1@gmail.com");

        Mockito.when(userRepository.findById(1)).thenReturn(Optional.of(this.users.get(0)));
        // Spring Security: mock method invocations to get current user
        Mockito.when(securityContext.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(securityContext);
        Mockito.when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(userDetails);
        Mockito.when(userDetails.getUsername()).thenReturn("username1");
        Mockito.when(userMapper.updateUserFromUserDTO(userDTO, this.users.get(0))).thenReturn(this.users.get(0));
        Mockito.when(userRepository.save(ArgumentMatchers.any(User.class))).thenReturn(this.users.get(0));
        Mockito.when(userMapper.toUserDTO(this.users.get(0))).thenReturn(userDTO);

        UserDTO actualUserDTO = userServiceImpl.updateUserById(1, userDTO);

        Assertions.assertEquals(actualUserDTO.getId(), userDTO.getId(), "Id should match each other");
        Assertions.assertEquals(actualUserDTO.getUsername(), userDTO.getUsername(), "Username should match each other");
        Assertions.assertEquals(actualUserDTO.getDob(), userDTO.getDob(), "Dob should match each other");
        Assertions.assertEquals(actualUserDTO.getEmail(), userDTO.getEmail(), "Email should match each other");
        Assertions.assertEquals(actualUserDTO.getRoles(), userDTO.getRoles(), "Roles should match each other");

        Mockito.verify(this.userRepository, Mockito.times(1)).save(this.users.get(0));
        Mockito.verify(this.userMapper, Mockito.times(1)).updateUserFromUserDTO(userDTO, this.users.get(0));
        Mockito.verify(this.userMapper, Mockito.times(1)).toUserDTO(this.users.get(0));
    }

    @Test
    @DisplayName("Update Existing User Should Throw Exception")
    void testUpdateUserFailed() {
        // Spring Security: mock SecurityContext object and related entities
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Authentication auth = Mockito.mock(Authentication.class);
        UserDetails userDetails = Mockito.mock(UserDetails.class);
        UserDTO userDTO = new UserDTO();
        userDTO.setDob(LocalDate.of(1999, 7, 2));
        userDTO.setEmail("newusername1@gmail.com");

        Mockito.when(userRepository.findById(1)).thenReturn(Optional.of(this.users.get(0)));
        // Spring Security: mock method invocations to get current user
        Mockito.when(securityContext.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(securityContext);
        Mockito.when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(userDetails);
        Mockito.when(userDetails.getUsername()).thenReturn("username123");

        Assertions.assertThrows(RuntimeException.class, () -> { userServiceImpl.updateUserById(1, userDTO); }, "Should throw exception");

        Mockito.verify(this.userRepository, Mockito.times(0)).save(this.users.get(0));
        Mockito.verify(this.userMapper, Mockito.times(0)).toUserDTO(this.users.get(0));
    }

    @Test
    @DisplayName("Delete Existing User Should Return Nothing (Successful)")
    void testDeleteUserSuccess() {
        List<Post> mockPosts = Mockito.mock();
        User userToDelete = this.users.get(3);
        // Spring Security: mock SecurityContext object and related entities
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Authentication auth = Mockito.mock(Authentication.class);
        UserDetails userDetails = Mockito.mock(UserDetails.class);

        Mockito.when(userRepository.findById(4)).thenReturn(Optional.of(userToDelete));
        // Spring Security: mock method invocations to get current user
        Mockito.when(securityContext.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(securityContext);
        Mockito.when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(userDetails);
        Mockito.when(userDetails.getUsername()).thenReturn("username4");
        userToDelete.setPosts(mockPosts);

        userServiceImpl.deleteUserById(4);

        Assertions.assertNull(userServiceImpl.findUserById(4), "Should not return any User");

        Mockito.verify(this.userRepository, Mockito.times(1)).delete(userToDelete);
    }

    @Test
    @DisplayName("Delete Existing User Should Throw Exception")
    void testDeleteUserFailed() {
        Mockito.doThrow(new ResourceNotFoundException("Could not find User with id - 0")).when(userRepository).findById(0);

        Assertions.assertThrows(ResourceNotFoundException.class, () -> { userServiceImpl.deleteUserById(0); }, "Should throw exception");

        Mockito.verify(this.userRepository, Mockito.times(1)).findById(0);
    }
}
