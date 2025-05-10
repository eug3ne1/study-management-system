package org.study.system.deepdivestudy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.study.system.deepdivestudy.model.users.Teacher;
import org.study.system.deepdivestudy.model.users.User;

import java.util.List;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {

    Teacher findByUser(User user);

    @Query("""
           SELECT t
           FROM   Teacher t
           JOIN   t.universities u
           WHERE  u.id = :universityId
           """)
    List<Teacher> findAllByUniversityId(@Param("universityId") Long universityId);
}



