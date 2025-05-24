package org.study.system.deepdivestudy.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.study.system.deepdivestudy.exceptions.DeadlineExpiredException;
import org.study.system.deepdivestudy.exceptions.TaskNotFoundException;
import org.study.system.deepdivestudy.entity.course.Course;
import org.study.system.deepdivestudy.entity.course.FilePath;
import org.study.system.deepdivestudy.entity.task.Task;
import org.study.system.deepdivestudy.entity.task.TaskSubmission;
import org.study.system.deepdivestudy.entity.users.Student;
import org.study.system.deepdivestudy.entity.users.Teacher;
import org.study.system.deepdivestudy.repository.TaskRepository;
import org.study.system.deepdivestudy.repository.TaskSubmissionRepository;
import org.study.system.deepdivestudy.service.*;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@AllArgsConstructor
public class TaskSubmissionServiceImpl implements TaskSubmissionService{

    private TaskSubmissionRepository repo;
    private TaskRepository taskRepo;
    private StorageService storage;
    private StudentService studentService;
    private TeacherService teacherService;
    private CourseService courseService;
    private GradeService gradeService;
    private TaskService taskService;

    @Override
    public TaskSubmission submit(Long taskId, String jwt) {
        Student studentByJWT = studentService.getStudentByJWT(jwt);

        Task task = taskRepo.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException("Task not found"));
        if (task.getEndTime().isBefore(LocalDateTime.now())){
            throw new DeadlineExpiredException("Deadline has expired");
        }
        Student student = studentService.findById(studentByJWT.getId());

        Optional<TaskSubmission> submissionByTaskAndStudent = repo.findByTaskAndStudent(task, student);
        if(submissionByTaskAndStudent.isPresent()){
            TaskSubmission submission = submissionByTaskAndStudent.get();
            submission.getFilesUrl().clear();
            return submission;
        }

        TaskSubmission sub = new TaskSubmission();
        sub.setTask(task);
        sub.setStudent(student);

        return repo.save(sub);
    }

    @Override
    public String uploadPdf(TaskSubmission submission, MultipartFile file, String jwt){
        Student studentByJWT = studentService.getStudentByJWT(jwt);
        if(!submission.getStudent().getId().equals(studentByJWT.getId())){
            throw new AccessDeniedException("You do not have permission to upload PDF for this task");
        }


        String pdfUrl = storage.saveTaskSubmission(submission.getId(), file);
        FilePath filePath = new FilePath();
        filePath.setPath(pdfUrl);

        submission.getFilesUrl().add(filePath);
        repo.save(submission);

        return pdfUrl;
    }

    @Override
    public void deleteFile(Long subId, Long fileId) {
        TaskSubmission submission = repo.findById(subId)
                .orElseThrow(() -> new RuntimeException("Submission not found"));

        FilePath filePath = submission.getFilesUrl().stream()
                .filter(f -> f.getId().equals(fileId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("File not found in lecture"));

        storage.deletePhysicalFile(filePath.getPath());
        submission.getFilesUrl().remove(filePath);
        repo.save(submission);
    }

    @Override
    public TaskSubmission grade(Long id, Double grade, String jwt) {
        Teacher teacherByJWT = teacherService.getTeacherByJWT(jwt);
        TaskSubmission sub = repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Submission not found"));
        if(!sub.getTask().getCourse().getTeacher().getId().equals(teacherByJWT.getId())){
            throw new AccessDeniedException("You do not have permission to grade this submission");
        }
        gradeService.addTaskGrade(grade, sub.getTask(), sub.getStudent());

        sub.setGrade(grade);

        return repo.save(sub);
    }

    @Override
    public TaskSubmission getById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Submission not found"));
    }

    @Override
    public List<TaskSubmission> findByCourse(Long courseId) {
        List<TaskSubmission> taskSubmissionList = new ArrayList<>();
        Course courseById = courseService.getCourseById(courseId);
        List<Task> tasks = courseById.getTasks();
        for (Task task : tasks) {
            taskSubmissionList.addAll(repo.findByTask_Id(task.getId()));
        }
        return taskSubmissionList;
    }

    @Override
    public Optional<TaskSubmission> findByTaskAndStudent(Long taskId, Student student) {
        Task task = taskService.getTaskById(taskId).orElseThrow(() -> new TaskNotFoundException("Task not found"));

        return repo.findByTaskAndStudent(task,student);
    }
}

