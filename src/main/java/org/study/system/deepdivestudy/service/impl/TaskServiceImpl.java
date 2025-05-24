package org.study.system.deepdivestudy.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.study.system.deepdivestudy.dto.TaskDTO;
import org.study.system.deepdivestudy.entity.course.Course;
import org.study.system.deepdivestudy.entity.course.FilePath;

import org.study.system.deepdivestudy.entity.task.Task;
import org.study.system.deepdivestudy.entity.users.Teacher;
import org.study.system.deepdivestudy.repository.TaskRepository;
import org.study.system.deepdivestudy.service.CourseService;
import org.study.system.deepdivestudy.service.StorageService;
import org.study.system.deepdivestudy.service.TaskService;
import org.study.system.deepdivestudy.service.TeacherService;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TaskServiceImpl implements TaskService {
    
    private final TaskRepository taskRepository;
    private final CourseService courseService;
    private final TeacherService teacherService;
    private final StorageService storageService;

    @Override
    public Task createTask(TaskDTO taskDTO, String jwt){
        Course course = courseService.getCourseById(taskDTO.getCourseId());

        Teacher teacher = teacherService.getTeacherByJWT(jwt);
        if (!course.getTeacher().getId().equals(teacher.getId())) {
            throw new AccessDeniedException("No permissions to create s for this course");
        }
        
        Task task = new Task();
        task.setTitle(taskDTO.getTitle());
        task.setDescription(taskDTO.getDescription());
        task.setStartTime(taskDTO.getStartTime());
        task.setEndTime(taskDTO.getEndTime());
        task.setMaxGrade(taskDTO.getMaxGrade());
        task.setCourse(course);
        
        return taskRepository.save(task);

    }

    @Override
    public List<Task> getAllTasksByCourse(Long courseId) {
        return taskRepository.findByCourse_Id(courseId);
    }

    @Override
    public Optional<Task> getTaskById(Long id) {
        return taskRepository.findById(id);
    }

    @Override
    public Task updateTask(Long taskId, TaskDTO taskDTO, String jwt) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("task not found"));

        Teacher teacher = teacherService.getTeacherByJWT(jwt);
        if (!task.getCourse().getTeacher().getId().equals(teacher.getId())) {
            throw new AccessDeniedException("You do not have permission to modify this ");
        }

        task.setTitle(taskDTO.getTitle());
        task.setDescription(taskDTO.getDescription());
        task.setStartTime(taskDTO.getStartTime());
        task.setEndTime(taskDTO.getEndTime());
        task.setMaxGrade(taskDTO.getMaxGrade());
        return taskRepository.save(task);
    }

    @Override
    public String uploadPdf(Long taskId, MultipartFile file, String jwt) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        Teacher teacher = teacherService.getTeacherByJWT(jwt);
        if (!task.getCourse().getTeacher().getId().equals(teacher.getId())) {
            throw new AccessDeniedException("You do not have permission to upload PDF for this ");
        }

        String pdfUrl = storageService.saveTaskPdf(taskId, file);
        FilePath filePath = new FilePath();
        filePath.setPath(pdfUrl);

        task.getFilesUrl().add(filePath);
        taskRepository.save(task);

        return pdfUrl;
    }

    @Override
    public void deleteFile(Long Id, Long fileId) {
        Task task  = taskRepository.findById(Id)
                .orElseThrow(() -> new RuntimeException("Task not found"));


        FilePath filePath = task.getFilesUrl().stream()
                .filter(f -> f.getId().equals(fileId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("File not found in "));

        storageService.deletePhysicalFile(filePath.getPath());

        task.getFilesUrl().remove(filePath);

        taskRepository.save(task);
    }

    @Override
    public void deleteTask(Long lectureId, String jwt) {
        Task task= taskRepository.findById(lectureId)
                .orElseThrow(() -> new RuntimeException("task not found"));
        Teacher teacher = teacherService.getTeacherByJWT(jwt);

        if (!task.getCourse().getTeacher().getId().equals(teacher.getId())) {
            throw new AccessDeniedException("No permissions to delete tasks for this course");
        }

        taskRepository.delete(task);
    }
}
