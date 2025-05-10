package org.study.system.deepdivestudy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.study.system.deepdivestudy.model.users.Role;
import org.study.system.deepdivestudy.model.users.RoleName;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(RoleName name);
}
