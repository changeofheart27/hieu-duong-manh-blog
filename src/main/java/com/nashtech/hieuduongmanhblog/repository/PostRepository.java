package com.nashtech.hieuduongmanhblog.repository;

import com.nashtech.hieuduongmanhblog.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {
    List<Post> findByUser_Username(String username);
}
