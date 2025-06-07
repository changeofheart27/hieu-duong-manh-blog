package vn.com.hieuduongmanhblog.repository;

import vn.com.hieuduongmanhblog.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.com.hieuduongmanhblog.entity.RoleName;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByRoleName(RoleName roleName);
}
