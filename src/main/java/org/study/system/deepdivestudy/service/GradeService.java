package org.study.system.deepdivestudy.service;

import org.study.system.deepdivestudy.dto.TaskGradeDTO;
import org.study.system.deepdivestudy.dto.TestGradeDTO;
import org.study.system.deepdivestudy.entity.task.Task;
import org.study.system.deepdivestudy.entity.users.Student;

import java.util.List;

public interface GradeService {

    List<TestGradeDTO> getAllTestGradesByCourse(Long courseId);

    List<TaskGradeDTO> getAllTaskGradesByCourse(Long courseId);

    void addTaskGrade(Double grade, Task task, Student student);

    List<TestGradeDTO> getStudentTestGrades(Long courseId, Long studentId);

    List<TaskGradeDTO> getStudentTaskGrades(Long courseId, Long studentId);

}
