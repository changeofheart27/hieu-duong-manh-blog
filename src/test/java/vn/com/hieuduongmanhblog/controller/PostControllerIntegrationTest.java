package vn.com.hieuduongmanhblog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import vn.com.hieuduongmanhblog.dto.PostDTO;
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

import java.time.LocalDateTime;

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
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", Matchers.is(200)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is("Get All Posts Successful")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.content", Matchers.hasSize(5)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.content[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.content[0].title").value("Demo Post Title 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.content[0].description").value("Demo Post Description 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.content[0].content").value("Demo Post Content 1"));
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
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", Matchers.is(200)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is("Get Post By ID Successful")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.title").value("Demo Post Title 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.description").value("Demo Post Description 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.content").value("Demo Post Content 1"));
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
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", Matchers.is(200)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is("Get Posts By Username Successful")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.content", Matchers.hasSize(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.content[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.content[0].postAuthor").value("hieuduongm"));
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
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", Matchers.is(200)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is("Get Posts By Username Successful")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.content", Matchers.hasSize(0)));
    }

    @Test
    @DisplayName("POST OPERATION: Create A New Post")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void testCreateNewPost() throws Exception {
        PostDTO createdPostDTO = new PostDTO(
                6,
                "Demo New Post Title 6",
                "Demo New Post Description 6",
                "Demo New Post Content 6",
                "#java"
        );

        this.mockMvc.perform(
                        MockMvcRequestBuilders
                                .post("/api/v1/posts")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(createdPostDTO))
                                .header(HttpHeaders.AUTHORIZATION, this.jwtToken)
                )
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", Matchers.is(201)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is("Create New Post Successful")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").value(createdPostDTO.id()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.title").value(createdPostDTO.title()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.description").value(createdPostDTO.description()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.content").value(createdPostDTO.content()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.postAuthor").value("hieuduongm"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.tags").value(createdPostDTO.tags()));

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
        PostDTO updatedPostDTO = new PostDTO(
                3,
                "Demo Edited Post Title 3",
                "Demo Edited Post Description 3",
                "Demo Edited Post Content 3",
                "#java"
        );

        this.mockMvc.perform(
                MockMvcRequestBuilders
                        .put("/api/v1/posts/{id}", updatedPostDTO.id())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedPostDTO))
                        .header(HttpHeaders.AUTHORIZATION, this.jwtToken)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", Matchers.is(200)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is("Update Post Successful")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").value(updatedPostDTO.id()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.title").value(updatedPostDTO.title()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.description").value(updatedPostDTO.description()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.content").value(updatedPostDTO.content()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.postAuthor").value("hieuduongm"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.tags").value(updatedPostDTO.tags()));

        this.mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/api/v1/posts/{id}", updatedPostDTO.id())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, this.jwtToken)
                )
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("PUT OPERATION: Update Existing Post Should Throw Exception")
    void testUpdatePostFailed() throws Exception {
        PostDTO updatedPostDTO = new PostDTO(
                null,
                "Demo Edited Post Title 10",
                "Demo Edited Post Description 10",
                "Demo Edited Post Content 10",
                "#java"
        );

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
