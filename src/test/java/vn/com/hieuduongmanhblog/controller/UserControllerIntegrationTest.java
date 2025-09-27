package vn.com.hieuduongmanhblog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
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
import vn.com.hieuduongmanhblog.dto.UserDTO;

import java.time.LocalDate;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("classpath:application-test.properties")
public class UserControllerIntegrationTest {
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
    @DisplayName("GET OPERATION: Get All Users")
    void testGetAllUsers() throws Exception {
        this.mockMvc.perform(
                        MockMvcRequestBuilders
                                .get("/api/v1/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header(HttpHeaders.AUTHORIZATION, this.jwtToken)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", Matchers.is(200)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is("Get All Users Successful")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.content", Matchers.hasSize(4)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.content[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.content[0].username").value("hieuduongm"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.content[0].dob").value(LocalDate.of(1999, 7, 2).toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.content[0].email").value("hieudhanu27@gmail.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.content[0].roles").isNotEmpty());
    }

    @Test
    @DisplayName("GET OPERATION: Find User By Id Should Return A Valid User")
    void testFindUserByIdSuccess() throws Exception {
        this.mockMvc.perform(
                        MockMvcRequestBuilders
                                .get("/api/v1/users/{id}", 1)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header(HttpHeaders.AUTHORIZATION, this.jwtToken)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", Matchers.is(200)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is("Get User By ID Successful")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.username").value("hieuduongm"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.dob").value(LocalDate.of(1999, 7, 2).toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.email").value("hieudhanu27@gmail.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.roles").isNotEmpty());
    }

    @Test
    @DisplayName("GET OPERATION: Find User By Id Should Throw Exception")
    void testFindUserByIdFailed() throws Exception {
        this.mockMvc.perform(
                        MockMvcRequestBuilders
                                .get("/api/v1/users/{id}", 0)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header(HttpHeaders.AUTHORIZATION, this.jwtToken)
                )
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", Matchers.is(404)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is("Could not find User with id - 0")));
    }

    @Test
    @DisplayName("GET OPERATION: Find User By username Should Return A Valid User")
    void testFindUserByUsernameSuccess() throws Exception {
        this.mockMvc.perform(
                        MockMvcRequestBuilders
                                .get("/api/v1/users")
                                .param("username", "tringuyenm")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header(HttpHeaders.AUTHORIZATION, this.jwtToken)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", Matchers.is(200)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is("Get User By username Successful")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.username").value("tringuyenm"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.dob").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.email").value("tringuyenm@gmail.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.roles").isNotEmpty());
    }

    @Test
    @DisplayName("GET OPERATION: Find User By username Should Throw Exception")
    void testFindUserByUsernameFailed() throws Exception {
        this.mockMvc.perform(
                        MockMvcRequestBuilders
                                .get("/api/v1/users")
                                .param("username", "username123")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header(HttpHeaders.AUTHORIZATION, this.jwtToken)
                )
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", Matchers.is(404)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is("Could not find User with username - username123")));
    }

    @Test
    @DisplayName("PUT OPERATION: Update Existing User Should Return Updated User")
    void testUpdateUserSuccess() throws Exception {
        UserDTO updatedUserDTO = new UserDTO(
                3,
                null,
                LocalDate.of(1999, 10, 20),
                "updatedemail@gmail.com",
                null
        );

        this.mockMvc.perform(
                        MockMvcRequestBuilders
                                .put("/api/v1/users/{id}", updatedUserDTO.id())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(updatedUserDTO))
                                .header(HttpHeaders.AUTHORIZATION, this.jwtToken)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", Matchers.is(200)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is("Update User Successful")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").value(updatedUserDTO.id()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.username").value("phuongcaot"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.dob").value(updatedUserDTO.dob().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.email").value(updatedUserDTO.email()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.roles").value("USER"));

        this.mockMvc.perform(
                        MockMvcRequestBuilders
                                .get("/api/v1/users/3")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header(HttpHeaders.AUTHORIZATION, this.jwtToken)
                )
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("PUT OPERATION: Update Existing User Should Throw Exception")
    void testUpdateUserFailed() throws Exception {
        UserDTO updatedUserDTO = new UserDTO(
                10,
                null,
                LocalDate.of(1999, 10, 20),
                "updatedemail@gmail.com",
                null
        );

        this.mockMvc.perform(
                        MockMvcRequestBuilders
                                .put("/api/v1/users/{id}", updatedUserDTO.id())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(this.objectMapper.writeValueAsString(updatedUserDTO))
                                .header(HttpHeaders.AUTHORIZATION, this.jwtToken)
                )
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", Matchers.is(404)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is("Could not find User with id - " + updatedUserDTO.id())));
    }

    @Test
    @DisplayName("DELETE OPERATION: Delete Existing User Should Return Nothing (Successful)")
    void testDeleteUserSuccess() throws Exception {
        this.mockMvc.perform(
                        MockMvcRequestBuilders
                                .delete("/api/v1/users/{id}", 4)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header(HttpHeaders.AUTHORIZATION, this.jwtToken)
                )
                .andExpect(MockMvcResultMatchers.status().isOk());

        this.mockMvc.perform(
                        MockMvcRequestBuilders
                                .get("/api/v1/users/{id}", 4)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header(HttpHeaders.AUTHORIZATION, this.jwtToken)
                )
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", Matchers.is(404)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is("Could not find User with id - 4")));
    }

    @Test
    @DisplayName("DELETE OPERATION: Delete Existing User Should Throw Exception")
    void testDeleteUserFailed() throws Exception {
        this.mockMvc.perform(
                        MockMvcRequestBuilders
                                .delete("/api/v1/users/{id}", 10)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header(HttpHeaders.AUTHORIZATION, this.jwtToken)
                )
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", Matchers.is(404)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is("Could not find User with id - 10")));
    }
}
