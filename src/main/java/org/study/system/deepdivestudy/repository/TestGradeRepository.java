package org.study.system.deepdivestudy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.study.system.deepdivestudy.entity.testing.TestGrade;

import java.util.List;
import java.util.Optional;

public interface TestGradeRepository extends JpaRepository<TestGrade, Long> {

    @Query("SELECT g FROM TestGrade g " +
            "WHERE g.student.id = :studentId " +
            "AND g.test.course.id = :courseId")
    List<TestGrade> findByStudentIdAndCourseId(@Param("studentId") Long studentId,
                                                   @Param("courseId") Long courseId);

    @Query("SELECT g FROM TestGrade g " +
            "WHERE g.student.id = :studentId " +
            "AND g.test.id = :testId")
    Optional<TestGrade> findByStudentIdAndTestId(Long studentId, Long testId);

    List<TestGrade> findByTest_Id(Long testId);



}
