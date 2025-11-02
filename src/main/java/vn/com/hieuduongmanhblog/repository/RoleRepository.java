package vn.com.hieuduongmanhblog.repository;

import vn.com.hieuduongmanhblog.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.com.hieuduongmanhblog.entity.RoleName;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByRoleName(RoleName roleName);

    List<Role> findAllByRoleNameIn(Set<RoleName> roleNames);
}
