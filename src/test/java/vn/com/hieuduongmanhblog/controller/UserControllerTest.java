package vn.com.hieuduongmanhblog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import vn.com.hieuduongmanhblog.dto.UserDTO;
import vn.com.hieuduongmanhblog.exception.ResourceNotFoundException;
import vn.com.hieuduongmanhblog.service.JwtUtilService;
import vn.com.hieuduongmanhblog.service.UserService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false) // Turn off Spring Security
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private JwtUtilService jwtUtilService;

    @Autowired
    private ObjectMapper objectMapper;

    private List<UserDTO> userDTOs;

    DateTimeFormatter dateTimeFormatter;

    @BeforeEach
    void setUp() {
        // prepare data to test
        UserDTO user1 = new UserDTO(1, "username1", LocalDate.of(1999, 7, 2), "username1@email.com", "ROLE_USER,ROLE_AUTHOR,ROLE_ADMIN");
        UserDTO user2 = new UserDTO(2, "username2", null, "username2@email.com", "ROLE_USER");
        UserDTO user3 = new UserDTO(3, "username3", null, "username3@email.com", "ROLE_USER");
        UserDTO user4 = new UserDTO(4, "username4", LocalDate.of(1999, 2, 7), "username4@email.com", "ROLE_USER");
        userDTOs = new ArrayList<>();
        userDTOs.add(user1);
        userDTOs.add(user2);
        userDTOs.add(user3);
        userDTOs.add(user4);

        dateTimeFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    }

    @AfterEach
    void tearDown() {
        // do nothing
    }

    @Test
    @DisplayName("GET OPERATION: Get All Users")
    void testGetAllUsers() throws Exception {
        int pageNumber = 0;
        int pageSize = 5;
        Page<UserDTO> userDTOsPage = new PageImpl<>(this.userDTOs);

        Mockito.when(userService.getAllUsers(ArgumentMatchers.anyInt(), ArgumentMatchers.anyInt()))
                .thenReturn(userDTOsPage);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users")
                    .param("page", String.valueOf(pageNumber))
                    .param("size", String.valueOf(pageSize)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Get All Users Successful"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.content[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.content[0].username").value("username1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.content[0].dob").value(LocalDate.of(1999, 7, 2).format(dateTimeFormatter)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.content[0].email").value("username1@email.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.content[0].roles").isNotEmpty());
    }

    @Test
    @DisplayName("GET OPERATION: Find User By Id Should Return A Valid User")
    void testFindUserByIdSuccess() throws Exception {
        Mockito.when(userService.findUserById(3)).thenReturn(this.userDTOs.get(2));

        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users/{id}", 3))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Get User By ID Successful"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").value(3))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.username").value("username3"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.dob").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.email").value("username3@email.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.roles").value("ROLE_USER"));
    }

    @Test
    @DisplayName("GET OPERATION: Find User By Id Should Throw Exception")
    void testFindUserByIdFailed() throws Exception {
        Mockito.when(userService.findUserById(0)).thenThrow(new ResourceNotFoundException("Could not find User with id - 0"));

        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users/{id}", 0))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", Matchers.is(404)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is("Could not find User with id - 0")));
    }

    @Test
    @DisplayName("GET OPERATION: Find Users By User username Should Return A Valid User")
    void testFindUserByUsernameSuccess() throws Exception {
        Mockito.when(userService.findUserByUsername("username2")).thenReturn(this.userDTOs.get(1));

        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users?username={username}", "username2"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Get User By username Successful"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.username").value("username2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.dob").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.email").value("username2@email.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.roles").value("ROLE_USER"));
    }

    @Test
    @DisplayName("PUT OPERATION: Update Existing User Should Return Updated User")
    void testUpdateUserSuccess() throws Exception {
        UserDTO updatedUserDTO = new UserDTO(4, "username4", LocalDate.of(1999, 2, 7), "updatedusername4@email.com", "ROLE_USER");

        // The mock call MUST use any instead of a concrete object
        Mockito.when(userService.updateUserById(ArgumentMatchers.anyInt(), ArgumentMatchers.any(UserDTO.class))).thenReturn(updatedUserDTO);

        this.mockMvc.perform(
                        MockMvcRequestBuilders
                                .put("/api/v1/users/{id}", 4)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(updatedUserDTO))
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").value(4))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.username").value(updatedUserDTO.getUsername()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.dob").value(updatedUserDTO.getDob().format(dateTimeFormatter)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.email").value(updatedUserDTO.getEmail()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.roles").value(updatedUserDTO.getRoles()));

        Mockito.verify(userService, Mockito.times(1)).updateUserById(ArgumentMatchers.anyInt(), ArgumentMatchers.any(UserDTO.class));
    }

    @Test
    @DisplayName("PUT OPERATION: Update Existing User Should Throw Exception")
    void testUpdateUserFailed() throws Exception {
        UserDTO invalidUserDTO = new UserDTO();
        invalidUserDTO.setUsername("username0");
        invalidUserDTO.setDob(LocalDate.of(2024, 6, 5));
        invalidUserDTO.setEmail("username0@email.com");
        invalidUserDTO.setCreatedAt(LocalDateTime.now());
        invalidUserDTO.setRoles("USER_ROLE");

        // The mock call MUST use any instead of a concrete object
        Mockito.when(userService.updateUserById(ArgumentMatchers.anyInt(), ArgumentMatchers.any(UserDTO.class)))
                .thenThrow(new ResourceNotFoundException("Could not find User with id - 0"));

        this.mockMvc.perform(
                        MockMvcRequestBuilders
                                .put("/api/v1/users/{id}", 0)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(invalidUserDTO))
                )
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", Matchers.is(404)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is("Could not find User with id - 0")));

        Mockito.verify(userService, Mockito.times(1)).updateUserById(ArgumentMatchers.anyInt(), ArgumentMatchers.any(UserDTO.class));
    }

    @Test
    @DisplayName("DELETE OPERATION: Delete Existing User Should Return Nothing (Successful)")
    void testDeleteUserSuccess() throws Exception {
        Mockito.doNothing().when(userService).deleteUserById(1);

        this.mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/users/{id}", 1))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("DELETE OPERATION: Delete Existing User Should Throw Exception")
    void testDeleteUserFailed() throws Exception {
        // The mock call MUST use any instead of a concrete object
        Mockito.doThrow(new ResourceNotFoundException("Could not find User with id - 0")).when(userService).deleteUserById(ArgumentMatchers.anyInt());

        this.mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/users/{id}", 0))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", Matchers.is(404)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is("Could not find User with id - 0")));
    }
}
