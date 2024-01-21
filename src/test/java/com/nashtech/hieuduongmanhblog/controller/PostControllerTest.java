package com.nashtech.hieuduongmanhblog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nashtech.hieuduongmanhblog.dto.PostDTO;
import com.nashtech.hieuduongmanhblog.entity.User;
import com.nashtech.hieuduongmanhblog.exception.ResourceNotFoundException;
import com.nashtech.hieuduongmanhblog.service.JwtUtilService;
import com.nashtech.hieuduongmanhblog.service.PostService;
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
import java.util.ArrayList;
import java.util.List;

@WebMvcTest(PostController.class)
@AutoConfigureMockMvc(addFilters = false) // Turn off Spring Security
public class PostControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PostService postService;

    @MockBean
    private JwtUtilService jwtUtilService;

    @Autowired
    private ObjectMapper objectMapper;

    private List<PostDTO> postDTOs;

    private PostDTO postDTOToCreate;

    private PostDTO postDTOToUpdate;

    private User user;

    @BeforeEach
    void setUp() {
        // prepare data to test
        objectMapper = new ObjectMapper();
        user = new User(1, "username", "password", LocalDate.of(1999, 7, 2), "username@email.com", LocalDate.now(), null);
        PostDTO post1 = new PostDTO(1, "Title 1", "Description 1", "Content 1", user.getUsername());
        PostDTO post2 = new PostDTO(2, "Title 2", "Description 2", "Content 2", user.getUsername());
        PostDTO post3 = new PostDTO(3, "Title 3", "Description 3", "Content 3", null);
        postDTOs = new ArrayList<>();
        postDTOs.add(post1);
        postDTOs.add(post2);
        postDTOs.add(post3);

        postDTOToCreate = new PostDTO(1, "New Post Title 1", "New Post Description 1", "New Post Content 1", user.getUsername());
        postDTOToUpdate = new PostDTO(1, "Updated Post Title 1", "Updated Post Description 1", "Updated Post Content 1", user.getUsername());
    }

    @AfterEach
    void tearDown() {
        // do nothing
    }

    @Test
    @DisplayName("GET OPERATION: Get All Posts")
    void testGetAllPosts() throws Exception {
        Mockito.when(postService.getAllPosts()).thenReturn(this.postDTOs);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/posts"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].title").value("Title 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].description").value("Description 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].content").value("Content 1"));
    }

    @Test
    @DisplayName("GET OPERATION: Find Post By Id Should Return A Valid Post")
    void testFindPostByIdSuccess() throws Exception {
        Mockito.when(postService.findPostById(1)).thenReturn(this.postDTOs.get(0));

        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/posts/{id}", 1))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Title 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Description 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").value("Content 1"));
    }

    @Test
    @DisplayName("GET OPERATION: Find Post By Id Should Throw Exception")
    void testFindPostByIdFailed() throws Exception {
        Mockito.when(postService.findPostById(0)).thenThrow(new ResourceNotFoundException("Could not find Post with id - 0"));

        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/posts/{id}", 0))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", Matchers.is(404)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is("Could not find Post with id - 0")));
    }

    @Test
    @DisplayName("GET OPERATION: Find Posts By User username Should Return List Of Valid Posts")
    void testFindPostByUserSuccess() throws Exception {
        this.postDTOs.remove(2);
        Mockito.when(postService.findPostsByUser("username")).thenReturn(this.postDTOs);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/posts?username={username}", "username"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].title").value("Title 2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].description").value("Description 2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].content").value("Content 2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].postAuthor").value("username"));
    }

    @Test
    @DisplayName("GET OPERATION: Find Posts By User username Should Return List Of Empty Post")
    void testFindPostByUserFailed() throws Exception {
        Mockito.when(postService.findPostsByUser(ArgumentMatchers.anyString())).thenReturn(new ArrayList<>());

        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/posts?username={username}", "username123"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(0)));
    }

    @Test
    @DisplayName("POST OPERATION: Create A New Post")
    void testCreateNewPost() throws Exception {
        PostDTO createdPostDTO = new PostDTO();
        createdPostDTO.setId(1);
        createdPostDTO.setTitle("New Post Title 1");
        createdPostDTO.setDescription("New Post Description 1");
        createdPostDTO.setContent("New Post Content 1");
        createdPostDTO.setPostAuthor("username");

        // The mock call MUST use any instead of a concrete object
        Mockito.when(postService.createPost(ArgumentMatchers.any(PostDTO.class))).thenReturn(createdPostDTO);


        this.mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/api/v1/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postDTOToCreate))
                )
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(createdPostDTO.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(createdPostDTO.getTitle()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value(createdPostDTO.getDescription()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").value(createdPostDTO.getContent()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.postAuthor").value(createdPostDTO.getPostAuthor()));

        Mockito.verify(postService, Mockito.times(1)).createPost(ArgumentMatchers.any(PostDTO.class));
    }

    @Test
    @DisplayName("PUT OPERATION: Update Existing Post Should Return Updated Post")
    void testUpdatePostSuccess() throws Exception {
        PostDTO updatedPostDTO = new PostDTO();
        updatedPostDTO.setId(1);
        updatedPostDTO.setTitle("Updated Post Title 1");
        updatedPostDTO.setDescription("Updated Post Description 1");
        updatedPostDTO.setContent("Updated Post Content 1");
        updatedPostDTO.setPostAuthor("username");

        // The mock call MUST use any instead of a concrete object
        Mockito.when(postService.updatePostById(ArgumentMatchers.anyInt(), ArgumentMatchers.any(PostDTO.class))).thenReturn(updatedPostDTO);

        this.mockMvc.perform(
                MockMvcRequestBuilders
                        .put("/api/v1/posts/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(postDTOToUpdate))
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(updatedPostDTO.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(updatedPostDTO.getTitle()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value(updatedPostDTO.getDescription()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").value(updatedPostDTO.getContent()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.postAuthor").value(updatedPostDTO.getPostAuthor()));

        Mockito.verify(postService, Mockito.times(1)).updatePostById(ArgumentMatchers.anyInt(), ArgumentMatchers.any(PostDTO.class));
    }

    @Test
    @DisplayName("PUT OPERATION: Update Existing Post Should Throw Exception")
    void testUpdatePostFailed() throws Exception {
        PostDTO updatedPostDTO = new PostDTO();
        updatedPostDTO.setId(1);
        updatedPostDTO.setTitle("Updated Post Title 1");
        updatedPostDTO.setDescription("Updated Post Description 1");
        updatedPostDTO.setContent("Updated Post Content 1");
        updatedPostDTO.setPostAuthor("username");

        // The mock call MUST use any instead of a concrete object
        Mockito.when(postService.updatePostById(ArgumentMatchers.anyInt(), ArgumentMatchers.any(PostDTO.class)))
                .thenThrow(new ResourceNotFoundException("Could not find Post with id - 0"));

        this.mockMvc.perform(
                MockMvcRequestBuilders
                        .put("/api/v1/posts/{id}", 0)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(postDTOToUpdate))
                )
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", Matchers.is(404)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is("Could not find Post with id - 0")));

        Mockito.verify(postService, Mockito.times(1)).updatePostById(ArgumentMatchers.anyInt(), ArgumentMatchers.any(PostDTO.class));
    }

    @Test
    @DisplayName("DELETE OPERATION: Delete Existing Post Should Return Nothing (Successful)")
    void testDeletePostSuccess() throws Exception {
        Mockito.doNothing().when(postService).deletePostById(1);

        this.mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/posts/{id}", 1))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("DELETE OPERATION: Delete Existing Post Should Throw Exception")
    void testDeletePostFailed() throws Exception {
        // The mock call MUST use any instead of a concrete object
        Mockito.doThrow(new ResourceNotFoundException("Could not find Post with id - 0")).when(postService).deletePostById(ArgumentMatchers.anyInt());

        this.mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/posts/{id}", 0))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", Matchers.is(404)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is("Could not find Post with id - 0")));
    }
}
