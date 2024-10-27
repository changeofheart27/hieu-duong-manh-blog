package vn.com.hieuduongmanhblog.service;

import vn.com.hieuduongmanhblog.dto.PostDTO;
import vn.com.hieuduongmanhblog.dto.mapper.PostMapper;
import vn.com.hieuduongmanhblog.dto.UserDTO;
import vn.com.hieuduongmanhblog.entity.Post;
import vn.com.hieuduongmanhblog.entity.User;
import vn.com.hieuduongmanhblog.exception.ResourceNotFoundException;
import vn.com.hieuduongmanhblog.repository.PostRepository;
import vn.com.hieuduongmanhblog.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import vn.com.hieuduongmanhblog.service.impl.PostServiceImpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class PostServiceImplTest {
    @Mock
    private PostRepository postRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PostMapper postMapper;

    @InjectMocks
    private PostServiceImpl postServiceImpl;

    private List<Post> posts;

    private Post postToCreate;

    private Post postToUpdate;

    private Post postToDelete;

    private User user;

    @BeforeEach
    void setUp() {
        // prepare data to test
        user = new User(1, "username", "password", LocalDate.of(1999, 7, 2), "username@email.com", LocalDateTime.now(), null, null);
        Post post1 = new Post(1, "Title 1", "Description 1", "Content 1", LocalDate.now(), user);
        Post post2 = new Post(2, "Title 2", "Description 2", "Content 2", LocalDate.now(), user);
        Post post3 = new Post(3, "Title 3", "Description 3", "Content 3", LocalDate.now(), null);
        posts = new ArrayList<>();
        posts.add(post1);
        posts.add(post2);
        posts.add(post3);

        postToCreate = new Post(1, "New Post Title 1", "New Post Description 1", "New Post Content 1", LocalDate.now(), user);
        postToUpdate = new Post(1, "Updated Post Title 1", "Updated Post Description 1", "Updated Post Content 1", LocalDate.now(), user);
    }

    @AfterEach
    void tearDown() {
        // do nothing
    }

    @Test
    @DisplayName("Get All Posts Should Return List Of Posts")
    void testGetAllPosts() {
        PostDTO mockPostDTO = Mockito.mock(PostDTO.class);
        Mockito.when(postRepository.findAll()).thenReturn(this.posts);
        Mockito.when(postMapper.toPostDTO(ArgumentMatchers.any(Post.class))).thenReturn(mockPostDTO);

        List<PostDTO> actualPostDTOs = postServiceImpl.getAllPosts();

        Assertions.assertEquals(actualPostDTOs.size(), this.posts.size(), "Size should be 3");

        Mockito.verify(this.postRepository, Mockito.times(1)).findAll();
        Mockito.verify(this.postMapper, Mockito.times(3)).toPostDTO(ArgumentMatchers.any(Post.class));
    }

    @Test
    @DisplayName("Find Post By Id Should Return A Valid Post")
    void testFindPostByIdSuccess() {
        PostDTO postDTO = new PostDTO(1, "Title 1", "Description 1", "Content 1", "Post Author");
        Mockito.when(postRepository.findById(1)).thenReturn(Optional.of(posts.get(0)));
        Mockito.when(postMapper.toPostDTO(ArgumentMatchers.any(Post.class))).thenReturn(postDTO);

        PostDTO actualPostDTO = postServiceImpl.findPostById(1);

        Assertions.assertEquals(actualPostDTO.getId(), this.posts.get(0).getId(), "Id should match each other");
        Assertions.assertEquals(actualPostDTO.getTitle(), this.posts.get(0).getTitle(), "Title should match each other");
        Assertions.assertEquals(actualPostDTO.getDescription(), this.posts.get(0).getDescription(), "Description should match each other");
        Assertions.assertEquals(actualPostDTO.getContent(), this.posts.get(0).getContent(), "Content should match each other");

        Mockito.verify(this.postRepository, Mockito.times(1)).findById(1);
        Mockito.verify(this.postMapper, Mockito.times(1)).toPostDTO(ArgumentMatchers.any(Post.class));
    }

    @Test
    @DisplayName("Find Post By Id Should Throw Exception")
    void testFindPostByIdFailed() {
        Mockito.when(postRepository.findById(10)).thenThrow(new ResourceNotFoundException("Could not find Post with id - 10"));

        Assertions.assertThrows(
                ResourceNotFoundException.class,
                () -> { postServiceImpl.findPostById(10); },
                "Should throw exception"
        );

        Mockito.verify(this.postRepository, Mockito.times(1)).findById(10);
        Mockito.verify(this.postMapper, Mockito.times(0)).toPostDTO(ArgumentMatchers.any(Post.class));
    }

    @Test
    @DisplayName("Find Post By User username Should Return List Of Valid Posts")
    void testFindPostByUserSuccess() {
        // remove post without user
        this.posts.remove(2);
        UserDTO userDTO = new UserDTO(1, "username", LocalDate.of(1999, 7, 2), "username@email.com", null);
        PostDTO postDTO = new PostDTO(1, "Title 1", "Description 1", "Content 1", userDTO.getUsername());
        Mockito.when(postRepository.findByUser_Username(ArgumentMatchers.anyString())).thenReturn(this.posts);
        Mockito.when(postMapper.toPostDTO(ArgumentMatchers.any(Post.class))).thenReturn(postDTO);

        List<PostDTO> actualPostDTOs = postServiceImpl.findPostsByUser("username");

        Assertions.assertEquals(actualPostDTOs.size(), this.posts.size(), "Size should be 2");
        Assertions.assertEquals(actualPostDTOs.get(0).getPostAuthor(), userDTO.getUsername(), "Username should match each other");

        Mockito.verify(this.postRepository, Mockito.times(1)).findByUser_Username(ArgumentMatchers.anyString());
        Mockito.verify(this.postMapper, Mockito.times(2)).toPostDTO(ArgumentMatchers.any(Post.class));
    }

    @Test
    @DisplayName("Find Post By User username Not Found Should Return List Of Empty Post")
    void testFindPostByUserFailed() {
        Mockito.when(postRepository.findByUser_Username(ArgumentMatchers.anyString())).thenReturn(new ArrayList<>());

        List<PostDTO> actualPostDTOs = postServiceImpl.findPostsByUser("username1");

        Assertions.assertEquals(actualPostDTOs.size(), 0, "Size should be 0");

        Mockito.verify(this.postRepository, Mockito.times(1)).findByUser_Username(ArgumentMatchers.anyString());
        Mockito.verify(this.postMapper, Mockito.times(0)).toPostDTO(ArgumentMatchers.any(Post.class));
    }

    @Test
    @DisplayName("Create A New Post")
    void testCreateNewPost() {
        // Spring Security: mock SecurityContext object and related entities
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Authentication auth = Mockito.mock(Authentication.class);
        UserDetails userDetails = Mockito.mock(UserDetails.class);
        PostDTO postDTO = new PostDTO();
        postDTO.setTitle("New Post Title 1");
        postDTO.setDescription("New Post Description 1");
        postDTO.setContent("New Post Content 1");

        Mockito.when(postMapper.toPost(postDTO)).thenReturn(postToCreate);
        // Spring Security: mock method invocations to get current user
        Mockito.when(securityContext.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(securityContext);
        Mockito.when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(userDetails);
        Mockito.when(userDetails.getUsername()).thenReturn("username");
        Mockito.when(userRepository.findByUsername("username")).thenReturn(Optional.of(user));
        Mockito.when(postRepository.save(ArgumentMatchers.any(Post.class))).thenReturn(postToCreate);
        Mockito.when(postMapper.toPostDTO(postToCreate)).thenReturn(postDTO);

        PostDTO actualPostDTO = postServiceImpl.createPost(postDTO);

        Assertions.assertEquals(actualPostDTO.getId(), postDTO.getId(), "Id should match each other");
        Assertions.assertEquals(actualPostDTO.getTitle(), postDTO.getTitle(), "Title should match each other");
        Assertions.assertEquals(actualPostDTO.getDescription(), postDTO.getDescription(), "Description should match each other");
        Assertions.assertEquals(actualPostDTO.getContent(), postDTO.getContent(), "Content should match each other");

        Mockito.verify(this.postRepository, Mockito.times(1)).save(ArgumentMatchers.any(Post.class));
        Mockito.verify(this.postMapper, Mockito.times(1)).toPost(ArgumentMatchers.any(PostDTO.class));
        Mockito.verify(this.postMapper, Mockito.times(1)).toPostDTO(postToCreate);
    }

    @Test
    @DisplayName("Update Existing Post Should Return Updated Post")
    void testUpdatePostSuccess() {
        // Spring Security: mock SecurityContext object and related entities
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Authentication auth = Mockito.mock(Authentication.class);
        UserDetails userDetails = Mockito.mock(UserDetails.class);
        PostDTO postDTO = new PostDTO();
        postDTO.setTitle("Updated Post Title 1");
        postDTO.setDescription("Updated Post Description 1");
        postDTO.setContent("Updated Post Content 1");

        Mockito.when(postRepository.findById(1)).thenReturn(Optional.of(postToUpdate));
        // Spring Security: mock method invocations to get current user
        Mockito.when(securityContext.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(securityContext);
        Mockito.when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(userDetails);
        Mockito.when(userDetails.getUsername()).thenReturn("username");
        Mockito.when(postMapper.updatePostFromPostDTO(postDTO, postToUpdate)).thenReturn(postToUpdate);
        Mockito.when(postRepository.save(ArgumentMatchers.any(Post.class))).thenReturn(postToUpdate);
        Mockito.when(postMapper.toPostDTO(postToUpdate)).thenReturn(postDTO);

        PostDTO actualPostDTO = postServiceImpl.updatePostById(1, postDTO);

        Assertions.assertEquals(actualPostDTO.getId(), postDTO.getId(), "Id should match each other");
        Assertions.assertEquals(actualPostDTO.getTitle(), postDTO.getTitle(), "Title should match each other");
        Assertions.assertEquals(actualPostDTO.getDescription(), postDTO.getDescription(), "Description should match each other");
        Assertions.assertEquals(actualPostDTO.getContent(), postDTO.getContent(), "Content should match each other");

        Mockito.verify(this.postRepository, Mockito.times(1)).save(postToUpdate);
        Mockito.verify(this.postMapper, Mockito.times(1)).updatePostFromPostDTO(postDTO, postToUpdate);
        Mockito.verify(this.postMapper, Mockito.times(1)).toPostDTO(postToUpdate);
    }

    @Test
    @DisplayName("Update Existing Post Should Throw Exception")
    void testUpdatePostFailed() {
        // Spring Security: mock SecurityContext object and related entities
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Authentication auth = Mockito.mock(Authentication.class);
        UserDetails userDetails = Mockito.mock(UserDetails.class);
        PostDTO postDTO = new PostDTO();
        postDTO.setTitle("Updated Post Title 1");
        postDTO.setDescription("Updated Post Description 1");
        postDTO.setContent("Updated Post Content 1");

        Mockito.when(postRepository.findById(1)).thenReturn(Optional.of(postToUpdate));
        // Spring Security: mock method invocations to get current user
        Mockito.when(securityContext.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(securityContext);
        Mockito.when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(userDetails);
        Mockito.when(userDetails.getUsername()).thenReturn("username123");

        Assertions.assertThrows(RuntimeException.class, () -> { postServiceImpl.updatePostById(1, postDTO); }, "Should throw exception");

        Mockito.verify(this.postRepository, Mockito.times(0)).save(postToUpdate);
        Mockito.verify(this.postMapper, Mockito.times(0)).toPostDTO(postToUpdate);
    }

    @Test
    @DisplayName("Delete Existing Post Should Return Nothing (Successful)")
    void testDeletePostSuccess() {
        // Spring Security: mock SecurityContext object and related entities
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Authentication auth = Mockito.mock(Authentication.class);
        UserDetails userDetails = Mockito.mock(UserDetails.class);

        Mockito.when(postRepository.findById(1)).thenReturn(Optional.of(this.posts.get(0)));
        // Spring Security: mock method invocations to get current user
        Mockito.when(securityContext.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(securityContext);
        Mockito.when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(userDetails);
        Mockito.when(userDetails.getUsername()).thenReturn("username");

        postServiceImpl.deletePostById(1);

        Assertions.assertNull(postServiceImpl.findPostById(1), "Should not return any Post");

        Mockito.verify(this.postRepository, Mockito.times(1)).delete(this.posts.get(0));
    }

    @Test
    @DisplayName("Delete Existing Post Should Throw Exception")
    void testDeletePostFailed() {
        Mockito.doThrow(new ResourceNotFoundException("Could not find Post with id - 0")).when(postRepository).findById(0);

        Assertions.assertThrows(ResourceNotFoundException.class, () -> { postServiceImpl.deletePostById(0); }, "Should throw exception");

        Mockito.verify(this.postRepository, Mockito.times(1)).findById(0);
    }
}
