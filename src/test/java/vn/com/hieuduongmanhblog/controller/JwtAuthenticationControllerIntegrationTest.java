package vn.com.hieuduongmanhblog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import vn.com.hieuduongmanhblog.dto.UserAuthenticationRequestDTO;
import vn.com.hieuduongmanhblog.dto.UserRegistrationRequestDTO;

import java.time.LocalDateTime;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("classpath:application-test.properties")
public class JwtAuthenticationControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String jwtToken;

    @BeforeEach
    void setUp() throws Exception {
        String loginAccountJson = "{\"username\": \"hieuduongm\",\"password\": \"test@123\"}";
        ResultActions resultActions = this.mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginAccountJson)
        );
        MvcResult mvcResult = resultActions.andDo(MockMvcResultHandlers.print()).andReturn();
        String jsonContent = mvcResult.getResponse().getContentAsString();
        JSONObject jsonObject = new JSONObject(jsonContent);
        this.jwtToken = "Bearer " + jsonObject.getString("data");
    }

    @AfterEach
    void tearDown() {
        // do nothing
    }

    @Test
    @DisplayName("POST OPERATION: Create A New User")
    void testRegisterNewUser() throws Exception {
        UserRegistrationRequestDTO requestDTO = new UserRegistrationRequestDTO("username1", "password1", "username1@email.com", LocalDateTime.now());

        this.mockMvc.perform(
                        MockMvcRequestBuilders
                                .post("/api/v1/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestDTO))
                                .header(HttpHeaders.AUTHORIZATION, this.jwtToken)
                )
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(201))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Register New User Successful"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isNotEmpty());
    }

    @Test
    @DisplayName("POST OPERATION: Authenticate Existing User")
    void testAuthenticateUser() throws Exception {
        UserAuthenticationRequestDTO requestDTO = new UserAuthenticationRequestDTO("hieuduongm", "test@123");

        // The mock call MUST use any instead of a concrete object
        this.mockMvc.perform(
                        MockMvcRequestBuilders
                                .post("/api/v1/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestDTO))
                                .header(HttpHeaders.AUTHORIZATION, this.jwtToken)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Login Successful"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isNotEmpty());
    }
}
