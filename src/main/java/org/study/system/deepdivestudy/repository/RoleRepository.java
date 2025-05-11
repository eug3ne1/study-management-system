package org.study.system.deepdivestudy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.study.system.deepdivestudy.entity.users.Role;
import org.study.system.deepdivestudy.entity.users.RoleName;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(RoleName name);
}
