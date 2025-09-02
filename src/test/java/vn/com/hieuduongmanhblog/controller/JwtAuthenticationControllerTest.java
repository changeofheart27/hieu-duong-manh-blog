package vn.com.hieuduongmanhblog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import vn.com.hieuduongmanhblog.dto.UserAuthenticationRequestDTO;
import vn.com.hieuduongmanhblog.dto.UserRegistrationRequestDTO;
import vn.com.hieuduongmanhblog.exception.JwtAuthenticationEntryPoint;
import vn.com.hieuduongmanhblog.service.AuthenticationService;
import vn.com.hieuduongmanhblog.service.JwtUtilService;

import java.time.LocalDateTime;

@WebMvcTest(JwtAuthenticationController.class)
@AutoConfigureMockMvc(addFilters = false) // Turn off Spring Security
public class JwtAuthenticationControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationService authenticationService;

    @MockBean
    private JwtUtilService jwtUtilService;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Test
    @DisplayName("POST OPERATION: Create A New User")
    void testRegisterNewUser() throws Exception {
        UserRegistrationRequestDTO requestDTO = new UserRegistrationRequestDTO("username1", "password1", "username1@email.com", LocalDateTime.now());
        String jwtToken = "jwttokenforusername1";

        // The mock call MUST use any instead of a concrete object
        Mockito.when(authenticationService.registerUser(ArgumentMatchers.any(UserRegistrationRequestDTO.class))).thenReturn(jwtToken);

        this.mockMvc.perform(
                        MockMvcRequestBuilders
                                .post("/api/v1/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestDTO))
                )
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(201))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Register New User Successful"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").value(jwtToken));

        Mockito.verify(authenticationService, Mockito.times(1)).registerUser(ArgumentMatchers.any(UserRegistrationRequestDTO.class));
    }

    @Test
    @DisplayName("POST OPERATION: Authenticate Existing User")
    void testAuthenticateUser() throws Exception {
        UserAuthenticationRequestDTO requestDTO = new UserAuthenticationRequestDTO("username1", "password1");
        String jwtToken = "jwttokenforusername1";

        // The mock call MUST use any instead of a concrete object
        Mockito.when(authenticationService.authenticateUser(ArgumentMatchers.any(UserAuthenticationRequestDTO.class))).thenReturn(jwtToken);

        this.mockMvc.perform(
                        MockMvcRequestBuilders
                                .post("/api/v1/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestDTO))
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Login Successful"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").value(jwtToken));

        Mockito.verify(authenticationService, Mockito.times(1)).authenticateUser(ArgumentMatchers.any(UserAuthenticationRequestDTO.class));
    }

}
