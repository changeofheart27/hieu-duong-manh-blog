package vn.com.hieuduongmanhblog.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import vn.com.hieuduongmanhblog.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {
    Page<Post> findByUser_Username(String username, PageRequest pageRequest);
}
