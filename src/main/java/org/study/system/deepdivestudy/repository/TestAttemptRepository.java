package org.study.system.deepdivestudy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.study.system.deepdivestudy.entity.testing.TestAttempt;


public interface TestAttemptRepository extends JpaRepository<TestAttempt, Long> {

    @Query("SELECT COUNT(*) from TestAttempt a WHERE a.student.id = :studentId AND a.test.id = :testId group by a.student, a.test ")
    Integer countTestAttemptsByStudentIdAndTestId(Long studentId, Long testId);

}

