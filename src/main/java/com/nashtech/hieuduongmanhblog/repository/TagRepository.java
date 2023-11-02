package com.nashtech.hieuduongmanhblog.repository;

import com.nashtech.hieuduongmanhblog.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends JpaRepository<Tag, Integer> {

}
