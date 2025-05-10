package org.study.system.deepdivestudy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.study.system.deepdivestudy.model.task.Task;
import org.study.system.deepdivestudy.model.task.TaskSubmission;
import org.study.system.deepdivestudy.model.users.Student;

import java.util.List;
import java.util.Optional;

public interface TaskSubmissionRepository extends JpaRepository<TaskSubmission, Long> {

    List<TaskSubmission> findByTask_Id(Long taskId);

    List<TaskSubmission> findByStudent_Id(Long studentId);

    Optional<TaskSubmission> findByTaskAndStudent(Task task, Student student);
}
