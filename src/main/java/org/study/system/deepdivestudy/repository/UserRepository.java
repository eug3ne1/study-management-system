package org.study.system.deepdivestudy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.study.system.deepdivestudy.entity.users.User;



public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);

}
