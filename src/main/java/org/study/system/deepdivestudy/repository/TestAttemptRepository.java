package org.study.system.deepdivestudy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.study.system.deepdivestudy.entity.testing.Test;
import org.study.system.deepdivestudy.entity.testing.TestAttempt;
import org.study.system.deepdivestudy.entity.users.Student;

import java.util.List;

public interface TestAttemptRepository extends JpaRepository<TestAttempt, Long> {
    List<TestAttempt> findByStudentAndTest(Student student, Test test);

    @Query("SELECT MAX(a.score) FROM TestAttempt a WHERE a.student = :student AND a.test = :test")
    Double findBestScore(@Param("student") Student student, @Param("test") Test test);

    @Query("SELECT COUNT(*) from TestAttempt a WHERE a.student.id = :studentId AND a.test.id = :testId group by a.student, a.test ")
    Integer countTestAttemptsByStudentIdAndTestId(Long studentId, Long testId);

}

