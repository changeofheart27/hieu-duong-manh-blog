package vn.com.hieuduongmanhblog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import vn.com.hieuduongmanhblog.dto.UserDTO;
import vn.com.hieuduongmanhblog.entity.Post;
import vn.com.hieuduongmanhblog.entity.Role;
import vn.com.hieuduongmanhblog.entity.User;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false) // Turn off Spring Security
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService UserService;

    @MockBean
    private JwtUtilService jwtUtilService;

    @Autowired
    private ObjectMapper objectMapper;

    private List<UserDTO> userDTOs;

    private UserDTO userDTOToCreate;

    private UserDTO userDTOToUpdate;

    private Post post;

    @BeforeEach
    void setUp() {
        // prepare data to test
        objectMapper = new ObjectMapper();
        Role userRole = new Role();
        userRole.setRoleName("ROLE_USER");
        Role authorRole = new Role();
        authorRole.setRoleName("ROLE_AUTHOR");
        Role adminRole = new Role();
        adminRole.setRoleName("ROLE_ADMIN");
        UserDTO user1 = new UserDTO(1, "username1", LocalDate.of(1999, 7, 2), "username1@email.com", LocalDateTime.now(), null, Set.of(userRole, authorRole, adminRole));
        UserDTO user2 = new UserDTO(2, "username2", null, "username2@email.com", LocalDateTime.now(), null, null);
        UserDTO user3 = new UserDTO(3, "username3", null, "username3@email.com", LocalDateTime.now(), null, null);
        post = new Post(3, "Title 3", "Description 3", "Content 3", LocalDateTime.now(), null, null);
        userDTOs = new ArrayList<>();
        userDTOs.add(user1);
        userDTOs.add(user2);
        userDTOs.add(user3);

        userDTOToCreate = new UserDTO(1, "New User Title 1", "New User Description 1", "New User Content 1", user.getUsername());
        userDTOToUpdate = new UserDTO(1, "Updated User Title 1", "Updated User Description 1", "Updated User Content 1", user.getUsername());
    }

    @AfterEach
    void tearDown() {
        // do nothing
    }

    @Test
    @DisplayName("GET OPERATION: Get All Users")
    void testGetAllUsers() throws Exception {
        Mockito.when(UserService.getAllUsers()).thenReturn(this.userDTOs);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Get All Users Successful!"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].username").value("username1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].dob").value(LocalDate.of(1999, 7, 2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].email").value("username1@email.com"));
    }

    @Test
    @DisplayName("GET OPERATION: Find User By Id Should Return A Valid User")
    void testFindUserByIdSuccess() throws Exception {
        Mockito.when(UserService.findUserById(1)).thenReturn(this.UserDTOs.get(0));

        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/Users/{id}", 1))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Title 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Description 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").value("Content 1"));
    }

    @Test
    @DisplayName("GET OPERATION: Find User By Id Should Throw Exception")
    void testFindUserByIdFailed() throws Exception {
        Mockito.when(UserService.findUserById(0)).thenThrow(new ResourceNotFoundException("Could not find User with id - 0"));

        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/Users/{id}", 0))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", Matchers.is(404)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is("Could not find User with id - 0")));
    }

    @Test
    @DisplayName("GET OPERATION: Find Users By User username Should Return List Of Valid Users")
    void testFindUserByUserSuccess() throws Exception {
        this.UserDTOs.remove(2);
        Mockito.when(UserService.findUsersByUser("username")).thenReturn(this.UserDTOs);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/Users?username={username}", "username"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].title").value("Title 2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].description").value("Description 2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].content").value("Content 2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].UserAuthor").value("username"));
    }

    @Test
    @DisplayName("GET OPERATION: Find Users By User username Should Return List Of Empty User")
    void testFindUserByUserFailed() throws Exception {
        Mockito.when(UserService.findUsersByUser(ArgumentMatchers.anyString())).thenReturn(new ArrayList<>());

        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/Users?username={username}", "username123"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(0)));
    }

    @Test
    @DisplayName("User OPERATION: Create A New User")
    void testCreateNewUser() throws Exception {
        UserDTO createdUserDTO = new UserDTO();
        createdUserDTO.setId(1);
        createdUserDTO.setTitle("New User Title 1");
        createdUserDTO.setDescription("New User Description 1");
        createdUserDTO.setContent("New User Content 1");
        createdUserDTO.setUserAuthor("username");

        // The mock call MUST use any instead of a concrete object
        Mockito.when(UserService.createUser(ArgumentMatchers.any(UserDTO.class))).thenReturn(createdUserDTO);


        this.mockMvc.perform(
                        MockMvcRequestBuilders
                                .User("/api/v1/Users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(UserDTOToCreate))
                )
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(createdUserDTO.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(createdUserDTO.getTitle()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value(createdUserDTO.getDescription()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").value(createdUserDTO.getContent()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.UserAuthor").value(createdUserDTO.getUserAuthor()));

        Mockito.verify(UserService, Mockito.times(1)).createUser(ArgumentMatchers.any(UserDTO.class));
    }

    @Test
    @DisplayName("PUT OPERATION: Update Existing User Should Return Updated User")
    void testUpdateUserSuccess() throws Exception {
        UserDTO updatedUserDTO = new UserDTO();
        updatedUserDTO.setId(1);
        updatedUserDTO.setTitle("Updated User Title 1");
        updatedUserDTO.setDescription("Updated User Description 1");
        updatedUserDTO.setContent("Updated User Content 1");
        updatedUserDTO.setUserAuthor("username");

        // The mock call MUST use any instead of a concrete object
        Mockito.when(UserService.updateUserById(ArgumentMatchers.anyInt(), ArgumentMatchers.any(UserDTO.class))).thenReturn(updatedUserDTO);

        this.mockMvc.perform(
                        MockMvcRequestBuilders
                                .put("/api/v1/Users/{id}", 1)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(this.objectMapper.writeValueAsString(UserDTOToUpdate))
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(updatedUserDTO.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(updatedUserDTO.getTitle()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value(updatedUserDTO.getDescription()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").value(updatedUserDTO.getContent()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.UserAuthor").value(updatedUserDTO.getUserAuthor()));

        Mockito.verify(UserService, Mockito.times(1)).updateUserById(ArgumentMatchers.anyInt(), ArgumentMatchers.any(UserDTO.class));
    }

    @Test
    @DisplayName("PUT OPERATION: Update Existing User Should Throw Exception")
    void testUpdateUserFailed() throws Exception {
        UserDTO updatedUserDTO = new UserDTO();
        updatedUserDTO.setId(1);
        updatedUserDTO.setTitle("Updated User Title 1");
        updatedUserDTO.setDescription("Updated User Description 1");
        updatedUserDTO.setContent("Updated User Content 1");
        updatedUserDTO.setUserAuthor("username");

        // The mock call MUST use any instead of a concrete object
        Mockito.when(UserService.updateUserById(ArgumentMatchers.anyInt(), ArgumentMatchers.any(UserDTO.class)))
                .thenThrow(new ResourceNotFoundException("Could not find User with id - 0"));

        this.mockMvc.perform(
                        MockMvcRequestBuilders
                                .put("/api/v1/Users/{id}", 0)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(this.objectMapper.writeValueAsString(UserDTOToUpdate))
                )
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", Matchers.is(404)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is("Could not find User with id - 0")));

        Mockito.verify(UserService, Mockito.times(1)).updateUserById(ArgumentMatchers.anyInt(), ArgumentMatchers.any(UserDTO.class));
    }

    @Test
    @DisplayName("DELETE OPERATION: Delete Existing User Should Return Nothing (Successful)")
    void testDeleteUserSuccess() throws Exception {
        Mockito.doNothing().when(UserService).deleteUserById(1);

        this.mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/Users/{id}", 1))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("DELETE OPERATION: Delete Existing User Should Throw Exception")
    void testDeleteUserFailed() throws Exception {
        // The mock call MUST use any instead of a concrete object
        Mockito.doThrow(new ResourceNotFoundException("Could not find User with id - 0")).when(UserService).deleteUserById(ArgumentMatchers.anyInt());

        this.mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/Users/{id}", 0))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", Matchers.is(404)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is("Could not find User with id - 0")));
    }
}
