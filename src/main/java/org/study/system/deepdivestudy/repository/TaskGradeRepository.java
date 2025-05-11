package org.study.system.deepdivestudy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.study.system.deepdivestudy.entity.task.TaskGrade;

import java.util.List;

public interface TaskGradeRepository extends JpaRepository<TaskGrade, Long> {

    List<TaskGrade> findByTask_Id(Long taskId);

    @Query("SELECT g FROM TaskGrade g " +
            "WHERE g.student.id = :studentId " +
            "AND g.task.course.id = :courseId")
    List<TaskGrade> findByStudentIdAndCourseId(@Param("studentId") Long studentId,
                                               @Param("courseId") Long courseId);

}
