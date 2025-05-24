package org.study.system.deepdivestudy.service;

import org.springframework.web.multipart.MultipartFile;
import org.study.system.deepdivestudy.dto.TaskDTO;
import org.study.system.deepdivestudy.entity.task.Task;

import java.util.List;
import java.util.Optional;

public interface TaskService {
    Task createTask(TaskDTO taskDTO, String jwt);

    List<Task> getAllTasksByCourse(Long courseId);

    Optional<Task> getTaskById(Long id);

    Task updateTask(Long taskId, TaskDTO taskDTO, String jwt);

    String uploadPdf(Long taskId, MultipartFile file, String jwt);

    void deleteFile(Long Id, Long fileId);

    void deleteTask(Long lectureId, String jwt);
}
