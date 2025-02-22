package vn.com.hieuduongmanhblog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import vn.com.hieuduongmanhblog.dto.PostDTO;
import vn.com.hieuduongmanhblog.entity.Role;
import vn.com.hieuduongmanhblog.entity.User;
import vn.com.hieuduongmanhblog.exception.ResourceNotFoundException;
import vn.com.hieuduongmanhblog.service.JwtUtilService;
import vn.com.hieuduongmanhblog.service.PostService;
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

    @BeforeEach
    void setUp() {
        // prepare data to test
        objectMapper = new ObjectMapper();
        User user = new User(1, "username", "password", LocalDate.of(1999, 7, 2), "username@email.com", LocalDateTime.now(), null, Set.of(new Role("ROLE_USER")));
        PostDTO post1 = new PostDTO(1, "Title 1", "Description 1", "Content 1", user.getUsername());
        PostDTO post2 = new PostDTO(2, "Title 2", "Description 2", "Content 2", user.getUsername());
        PostDTO post3 = new PostDTO(3, "Title 3", "Description 3", "Content 3", null);
        postDTOs = new ArrayList<>();
        postDTOs.add(post1);
        postDTOs.add(post2);
        postDTOs.add(post3);
    }

    @AfterEach
    void tearDown() {
        // do nothing
    }

    @Test
    @DisplayName("GET OPERATION: Get All Posts")
    void testGetAllPosts() throws Exception {
        int pageNumber = 0;
        int pageSize = 5;
        Page<PostDTO> postDTOsPage = new PageImpl<>(this.postDTOs);

        Mockito.when(
                postService.getAllPosts(ArgumentMatchers.anyInt(), ArgumentMatchers.anyInt())
        ).thenReturn(postDTOsPage);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/posts")
                        .param("page", String.valueOf(pageNumber))
                        .param("size", String.valueOf(pageSize)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Get All Posts Successful"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data", Matchers.hasSize(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].title").value("Title 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].description").value("Description 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].content").value("Content 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].postAuthor").value("username"));
    }

    @Test
    @DisplayName("GET OPERATION: Find Post By Id Should Return A Valid Post")
    void testFindPostByIdSuccess() throws Exception {
        Mockito.when(postService.findPostById(1)).thenReturn(this.postDTOs.get(0));

        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/posts/{id}", 1))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Get Post By ID Successful"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.title").value("Title 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.description").value("Description 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.content").value("Content 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.postAuthor").value("username"));
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
        int pageNumber = 0;
        int pageSize = 5;
        Page<PostDTO> postDTOsPage = new PageImpl<>(this.postDTOs);

        Mockito.when(
                postService.findPostsByUser(
                        ArgumentMatchers.anyString(),
                        ArgumentMatchers.anyInt(),
                        ArgumentMatchers.anyInt()
                )).thenReturn(postDTOsPage);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/posts?username={username}", "username")
                        .param("page", String.valueOf(pageNumber))
                        .param("size", String.valueOf(pageSize)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Get Posts By Username Successful"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data", Matchers.hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[1].id").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[1].title").value("Title 2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[1].description").value("Description 2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[1].content").value("Content 2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[1].postAuthor").value("username"));
    }

    @Test
    @DisplayName("GET OPERATION: Find Posts By User username Should Return List Of Empty Post")
    void testFindPostByUserFailed() throws Exception {
        int pageNumber = 0;
        int pageSize = 5;
        Page<PostDTO> postDTOsPage = new PageImpl<>(new ArrayList<>());
        Mockito.when(
                postService.findPostsByUser(
                        ArgumentMatchers.anyString(),
                        ArgumentMatchers.anyInt(),
                        ArgumentMatchers.anyInt()
                )).thenReturn(postDTOsPage);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/posts?username={username}", "username123")
                        .param("page", String.valueOf(pageNumber))
                        .param("size", String.valueOf(pageSize)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Get Posts By Username Successful"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data", Matchers.hasSize(0)));
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
                        .content(objectMapper.writeValueAsString(createdPostDTO))
                )
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(201))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Create New Post Successful"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").value(createdPostDTO.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.title").value(createdPostDTO.getTitle()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.description").value(createdPostDTO.getDescription()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.content").value(createdPostDTO.getContent()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.postAuthor").value(createdPostDTO.getPostAuthor()));

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
                        .content(this.objectMapper.writeValueAsString(updatedPostDTO))
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Update Post Successful"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").value(updatedPostDTO.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.title").value(updatedPostDTO.getTitle()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.description").value(updatedPostDTO.getDescription()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.content").value(updatedPostDTO.getContent()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.postAuthor").value(updatedPostDTO.getPostAuthor()));

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
                        .content(this.objectMapper.writeValueAsString(updatedPostDTO))
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
