package vn.com.hieuduongmanhblog.repository;

import vn.com.hieuduongmanhblog.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findByRoleName(String roleName);
}
