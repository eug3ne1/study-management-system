package org.study.system.deepdivestudy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.study.system.deepdivestudy.model.users.Student;
import org.study.system.deepdivestudy.model.users.User;


public interface StudentRepository extends JpaRepository<Student,Long> {

    Student findByUser(User user);
}
