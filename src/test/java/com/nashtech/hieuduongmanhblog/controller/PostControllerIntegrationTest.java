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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
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
    @DisplayName("HTTP GET REQUEST To Get All Post")
    @Order(1)
    void testGetAllPosts() throws Exception {
        this.mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/api/v1/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, this.jwtToken)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].title").value("Java 17 New Features"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].description").value("Java 17, the latest (3rd) LTS, was released on September 14, 2021. What are the new features? Find out now."))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].content").value("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Maecenas sed eleifend odio. Nam sodales diam efficitur convallis porttitor. In vel arcu nibh. Quisque vel volutpat urna, ac viverra neque. Suspendisse pellentesque feugiat augue. Ut porttitor purus id urna condimentum, non dapibus arcu dignissim. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Maecenas tincidunt magna vitae faucibus mattis. Ut lacinia nisi non quam pharetra, id cursus purus commodo. Duis et lobortis nibh, et viverra odio. Maecenas blandit posuere velit, id hendrerit leo euismod non. Suspendisse aliquet lorem libero, at maximus odio scelerisque vitae. Sed urna leo, molestie eget fermentum gravida, consequat nec lectus. Maecenas id laoreet ligula. Morbi magna tellus, fermentum non elementum at, viverra a metus. Ut sed velit sollicitudin, rhoncus nisi eu, ultricies diam."));
    }

    @Test
    @DisplayName("HTTP GET REQUEST To Get Post By Id Should Return A Valid Post")
    @Order(2)
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
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Java 17 New Features"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Java 17, the latest (3rd) LTS, was released on September 14, 2021. What are the new features? Find out now."))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").value("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Maecenas sed eleifend odio. Nam sodales diam efficitur convallis porttitor. In vel arcu nibh. Quisque vel volutpat urna, ac viverra neque. Suspendisse pellentesque feugiat augue. Ut porttitor purus id urna condimentum, non dapibus arcu dignissim. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Maecenas tincidunt magna vitae faucibus mattis. Ut lacinia nisi non quam pharetra, id cursus purus commodo. Duis et lobortis nibh, et viverra odio. Maecenas blandit posuere velit, id hendrerit leo euismod non. Suspendisse aliquet lorem libero, at maximus odio scelerisque vitae. Sed urna leo, molestie eget fermentum gravida, consequat nec lectus. Maecenas id laoreet ligula. Morbi magna tellus, fermentum non elementum at, viverra a metus. Ut sed velit sollicitudin, rhoncus nisi eu, ultricies diam."));
    }

    @Test
    @DisplayName("HTTP GET REQUEST To Get Post By Id Should Throw Exception")
    @Order(3)
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
    @DisplayName("HTTP GET REQUEST To Get Post By User username Should Return List Of Valid Posts")
    @Order(4)
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
    @DisplayName("HTTP GET REQUEST To Get Post By User username Should Return List Of Empty Post")
    @Order(5)
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
    @DisplayName("HTTP POST REQUEST To Create A New Post")
    @Order(6)
    void testCreateNewPost() throws Exception {
        PostDTO createdPostDTO = new PostDTO();
        createdPostDTO.setTitle("Spring Boot Unit testing with JUnit and Mockito");
        createdPostDTO.setDescription("In this tutorial we will learn how to perform unit testing Spring boot CRUD RESTful web services using JUnit 5 and Mockito framework.");
        createdPostDTO.setContent("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vestibulum tempor sagittis enim, a accumsan lorem laoreet sit amet. Maecenas pharetra rutrum tempus. Pellentesque dapibus dolor ut lorem sagittis pulvinar. Mauris pellentesque varius quam. Morbi viverra dolor rutrum lorem consequat scelerisque nec vitae ex. Praesent accumsan, odio ac posuere ultricies, arcu sem malesuada metus, id malesuada arcu enim eu neque. Proin at lacinia orci, at sodales est. Nulla sollicitudin placerat tristique. Vivamus a dolor at lacus pharetra faucibus. Etiam et eleifend dui. Sed finibus nibh velit, non laoreet erat lobortis non. Sed commodo metus mi, at consectetur est aliquet in. In scelerisque vestibulum metus. Sed convallis magna et bibendum finibus.");

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
                        .get("/api/v1/posts/4")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, this.jwtToken)
                )
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("HTTP PUT REQUEST To Update Existing Post Should Return Updated Post")
    @Order(7)
    void testUpdatePostSuccess() throws Exception {
        PostDTO updatedPostDTO = new PostDTO();
        updatedPostDTO.setTitle("Spring Boot Unit testing with JUnit and Mockito Edited Title");
        updatedPostDTO.setDescription("In this tutorial we will learn how to perform unit testing Spring boot CRUD RESTful web services using JUnit 5 and Mockito framework. Edited Description");
        updatedPostDTO.setContent("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vestibulum tempor sagittis enim, a accumsan lorem laoreet sit amet. Maecenas pharetra rutrum tempus. Pellentesque dapibus dolor ut lorem sagittis pulvinar. Mauris pellentesque varius quam. Morbi viverra dolor rutrum lorem consequat scelerisque nec vitae ex. Praesent accumsan, odio ac posuere ultricies, arcu sem malesuada metus, id malesuada arcu enim eu neque. Proin at lacinia orci, at sodales est. Nulla sollicitudin placerat tristique. Vivamus a dolor at lacus pharetra faucibus. Etiam et eleifend dui. Sed finibus nibh velit, non laoreet erat lobortis non. Sed commodo metus mi, at consectetur est aliquet in. In scelerisque vestibulum metus. Sed convallis magna et bibendum finibus. Edited Content");

        this.mockMvc.perform(
                MockMvcRequestBuilders
                        .put("/api/v1/posts/{id}", 4)
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
                        .get("/api/v1/posts/4")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, this.jwtToken)
                )
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("HTTP PUT REQUEST To Update Existing Post Should Throw Exception")
    @Order(8)
    void testUpdatePostFailed() throws Exception {
        PostDTO updatedPostDTO = new PostDTO();
        updatedPostDTO.setTitle("Spring Boot Unit testing with JUnit and Mockito Edited Title");
        updatedPostDTO.setDescription("In this tutorial we will learn how to perform unit testing Spring boot CRUD RESTful web services using JUnit 5 and Mockito framework. Edited Description");
        updatedPostDTO.setContent("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vestibulum tempor sagittis enim, a accumsan lorem laoreet sit amet. Maecenas pharetra rutrum tempus. Pellentesque dapibus dolor ut lorem sagittis pulvinar. Mauris pellentesque varius quam. Morbi viverra dolor rutrum lorem consequat scelerisque nec vitae ex. Praesent accumsan, odio ac posuere ultricies, arcu sem malesuada metus, id malesuada arcu enim eu neque. Proin at lacinia orci, at sodales est. Nulla sollicitudin placerat tristique. Vivamus a dolor at lacus pharetra faucibus. Etiam et eleifend dui. Sed finibus nibh velit, non laoreet erat lobortis non. Sed commodo metus mi, at consectetur est aliquet in. In scelerisque vestibulum metus. Sed convallis magna et bibendum finibus. Edited Content");

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
    @DisplayName("HTTP DELETE REQUEST To Delete Existing Post Should Return Nothing (Successful)")
    @Order(9)
    void testDeletePostSuccess() throws Exception {
        this.mockMvc.perform(
                MockMvcRequestBuilders
                        .delete("/api/v1/posts/{id}", 4)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, this.jwtToken))
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
}
