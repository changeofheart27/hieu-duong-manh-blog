package com.nashtech.hieuduongmanhblog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nashtech.hieuduongmanhblog.dto.PostDTO;
import org.hamcrest.Matchers;
import org.json.JSONObject;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("classpath:application-test.properties")
public class PostControllerIntegrationTest {
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
                        .post("/api/v1/auth/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginAccountJson)
        );
        MvcResult mvcResult = resultActions.andDo(MockMvcResultHandlers.print()).andReturn();
        String jsonContent = mvcResult.getResponse().getContentAsString();
        JSONObject jsonObject = new JSONObject(jsonContent);
        this.jwtToken = "Bearer " + jsonObject.getString("jwtToken");
    }

    @AfterEach
    void tearDown() {
        // do nothing
    }

    @Test
    @DisplayName("GET OPERATION: Get All Posts")
    void testGetAllPosts() throws Exception {
        this.mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/api/v1/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, this.jwtToken)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(5)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].title").value("Demo Post Title 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].description").value("Demo Post Description 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].content").value("Demo Post Content 1"));
    }

    @Test
    @DisplayName("GET OPERATION: Find Post By Id Should Return A Valid Post")
    void testFindPostByIdSuccess() throws Exception {
        this.mockMvc.perform(
                        MockMvcRequestBuilders
                                .get("/api/v1/posts/{id}", 1)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header(HttpHeaders.AUTHORIZATION, this.jwtToken)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Demo Post Title 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Demo Post Description 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").value("Demo Post Content 1"));
    }

    @Test
    @DisplayName("GET OPERATION: Find Post By Id Should Throw Exception")
    void testFindPostByIdFailed() throws Exception {
        this.mockMvc.perform(
                        MockMvcRequestBuilders
                                .get("/api/v1/posts/{id}", 0)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header(HttpHeaders.AUTHORIZATION, this.jwtToken)
                )
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", Matchers.is(404)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is("Could not find Post with id - 0")));
    }

    @Test
    @DisplayName("GET OPERATION: Find Posts By User username Should Return List Of Valid Posts")
    void testFindPostByUserSuccess() throws Exception {
        this.mockMvc.perform(
                        MockMvcRequestBuilders
                                .get("/api/v1/posts")
                                .param("username", "hieuduongm")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header(HttpHeaders.AUTHORIZATION, this.jwtToken)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].postAuthor").value("hieuduongm"));
    }

    @Test
    @DisplayName("GET OPERATION: Find Posts By User username Should Return List Of Empty Post")
    void testFindPostByUserFailed() throws Exception {
        this.mockMvc.perform(
                        MockMvcRequestBuilders
                                .get("/api/v1/posts")
                                .param("username", "username123")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header(HttpHeaders.AUTHORIZATION, this.jwtToken)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(0)));
    }

    @Test
    @DisplayName("POST OPERATION: Create A New Post")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void testCreateNewPost() throws Exception {
        PostDTO createdPostDTO = new PostDTO();
        createdPostDTO.setId(6);
        createdPostDTO.setTitle("Demo New Post Title 6");
        createdPostDTO.setDescription("Demo New Post Description 6");
        createdPostDTO.setContent("Demo New Post Content 6");

        this.mockMvc.perform(
                        MockMvcRequestBuilders
                                .post("/api/v1/posts")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(createdPostDTO))
                                .header(HttpHeaders.AUTHORIZATION, this.jwtToken)
                )
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(createdPostDTO.getTitle()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value(createdPostDTO.getDescription()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").value(createdPostDTO.getContent()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.postAuthor").value("hieuduongm"));

        this.mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/api/v1/posts/6")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, this.jwtToken)
                )
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("PUT OPERATION: Update Existing Post Should Return Updated Post")
    void testUpdatePostSuccess() throws Exception {
        PostDTO updatedPostDTO = new PostDTO();
        updatedPostDTO.setTitle("Demo Edited Post Title 3");
        updatedPostDTO.setDescription("Demo Edited Post Description 3");
        updatedPostDTO.setContent("Demo Edited Post Content 3");

        this.mockMvc.perform(
                MockMvcRequestBuilders
                        .put("/api/v1/posts/{id}", 3)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedPostDTO))
                        .header(HttpHeaders.AUTHORIZATION, this.jwtToken)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(updatedPostDTO.getTitle()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value(updatedPostDTO.getDescription()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").value(updatedPostDTO.getContent()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.postAuthor").value("hieuduongm"));

        this.mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/api/v1/posts/3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, this.jwtToken)
                )
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("PUT OPERATION: Update Existing Post Should Throw Exception")
    void testUpdatePostFailed() throws Exception {
        PostDTO updatedPostDTO = new PostDTO();
        updatedPostDTO.setTitle("Demo Edited Post Title 10");
        updatedPostDTO.setDescription("Demo Edited Post Description 10");
        updatedPostDTO.setContent("Demo Edited Post Content 10");

        this.mockMvc.perform(
                MockMvcRequestBuilders
                        .put("/api/v1/posts/{id}", 10)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(updatedPostDTO))
                        .header(HttpHeaders.AUTHORIZATION, this.jwtToken)
                )
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", Matchers.is(404)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is("Could not find Post with id - 10")));
    }

    @Test
    @DisplayName("DELETE OPERATION: Delete Existing Post Should Return Nothing (Successful)")
    void testDeletePostSuccess() throws Exception {
        this.mockMvc.perform(
                MockMvcRequestBuilders
                        .delete("/api/v1/posts/{id}", 4)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, this.jwtToken)
                )
                .andExpect(MockMvcResultMatchers.status().isOk());

        this.mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/api/v1/posts/{id}", 4)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, this.jwtToken)
                )
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", Matchers.is(404)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is("Could not find Post with id - 4")));
    }

    @Test
    @DisplayName("DELETE OPERATION: Delete Existing Post Should Throw Exception")
    void testDeletePostFailed() throws Exception {
        this.mockMvc.perform(
                MockMvcRequestBuilders
                        .delete("/api/v1/posts/{id}", 10)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, this.jwtToken)
                )
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", Matchers.is(404)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is("Could not find Post with id - 10")));
    }
}
