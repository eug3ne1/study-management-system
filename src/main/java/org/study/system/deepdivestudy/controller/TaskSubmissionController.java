package org.study.system.deepdivestudy.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.study.system.deepdivestudy.dto.LectureDTO;
import org.study.system.deepdivestudy.model.course.Lecture;
import org.study.system.deepdivestudy.model.task.TaskSubmission;
import org.study.system.deepdivestudy.model.users.Student;
import org.study.system.deepdivestudy.service.StudentService;
import org.study.system.deepdivestudy.service.TaskSubmissionService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/task-submission")
@AllArgsConstructor
@Slf4j
public class TaskSubmissionController {
    private final TaskSubmissionService taskSubmissionService;
    private final StudentService studentService;

    @GetMapping("/{courseId}")
    public ResponseEntity<List<TaskSubmission>> getSubsByCourse (@PathVariable Long courseId){
        List<TaskSubmission> taskSubmissions = taskSubmissionService.findByCourse(courseId);
        return new ResponseEntity<>(taskSubmissions, HttpStatus.OK);
    }


    @GetMapping("/{taskId}/student")
    public ResponseEntity<TaskSubmission> getSubByStudentAndTask(
            @PathVariable Long taskId,
            @RequestHeader("Authorization") String jwt) {

        Student student = studentService.getStudentByJWT(jwt);
        return taskSubmissionService
                .findByTaskAndStudent(taskId, student)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{subId}/grade")
    public ResponseEntity<TaskSubmission> gradeSub(@PathVariable Long subId, @RequestParam("grade") Double grade,
                                                   @RequestHeader("Authorization") String jwt){
        TaskSubmission submission = taskSubmissionService.grade(subId, grade, jwt);
        return new ResponseEntity<>(submission, HttpStatus.OK);
    }

    @PostMapping("/{taskId}/create")
    public ResponseEntity<TaskSubmission> createSub(
                                            @PathVariable Long taskId,
                                            @RequestPart(value = "file", required = false) MultipartFile[] files,
                                            @RequestHeader("Authorization") String jwt) {


        TaskSubmission submission = taskSubmissionService.submit(taskId, jwt);

        if (files != null) {
            for (MultipartFile file : files) {
                taskSubmissionService.uploadPdf(submission, file, jwt);
            }
        }
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(submission);
    }


    @PutMapping("/{subId}/update")
    public ResponseEntity<TaskSubmission> updateSub(
            @PathVariable Long subId,
            @RequestPart(value = "file", required = false) MultipartFile[] files,
            @RequestHeader("Authorization") String jwt) {

        TaskSubmission submission = taskSubmissionService.getById(subId);

        if (files != null) {
            for (MultipartFile file : files) {
                taskSubmissionService.uploadPdf(submission, file, jwt);
            }
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(submission);
    }


    @DeleteMapping("/{subId}/file/{fileId}")
    public ResponseEntity<String> deleteFile(@PathVariable Long subId, @PathVariable Long fileId){
        taskSubmissionService.deleteFile(subId, fileId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}







