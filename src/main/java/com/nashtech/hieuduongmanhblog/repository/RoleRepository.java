package com.nashtech.hieuduongmanhblog.repository;

import com.nashtech.hieuduongmanhblog.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findByName(String name);
}
