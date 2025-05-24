package org.study.system.deepdivestudy.service;

import org.springframework.web.multipart.MultipartFile;
import org.study.system.deepdivestudy.entity.task.TaskSubmission;
import org.study.system.deepdivestudy.entity.users.Student;

import java.util.List;
import java.util.Optional;

public interface TaskSubmissionService {
    TaskSubmission submit(Long taskId, String jwt);

    String uploadPdf(TaskSubmission submission, MultipartFile file, String jwt);

    void deleteFile(Long subId, Long fileId);

    TaskSubmission grade(Long id, Double grade, String jwt);

    TaskSubmission getById(Long id);

    List<TaskSubmission> findByCourse(Long courseId);

    Optional<TaskSubmission> findByTaskAndStudent(Long taskId, Student student);
}
