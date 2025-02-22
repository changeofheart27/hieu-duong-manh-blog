package vn.com.hieuduongmanhblog.service;

import org.springframework.data.domain.Page;
import vn.com.hieuduongmanhblog.dto.PostDTO;

public interface PostService {
    Page<PostDTO> getAllPosts(int pageNumber, int pageSize);

    PostDTO findPostById(int postId);

    Page<PostDTO> findPostsByUser(String username, int pageNumber, int pageSize);

    PostDTO createPost(PostDTO newPost);

    PostDTO updatePostById(int postId, PostDTO newPost);

    void deletePostById(int postId);
}
