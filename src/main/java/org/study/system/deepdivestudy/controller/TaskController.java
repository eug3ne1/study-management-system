package org.study.system.deepdivestudy.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.study.system.deepdivestudy.dto.TaskDTO;
import org.study.system.deepdivestudy.entity.task.Task;
import org.study.system.deepdivestudy.service.TaskService;

import java.util.List;

@RestController()
@RequestMapping("/api/tasks")
@AllArgsConstructor
public class TaskController {

    private final TaskService taskService;


    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<Task>> getAllTasks(@PathVariable Long courseId) {
        return new ResponseEntity<>(taskService.getAllTasksByCourse(courseId), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTask(@PathVariable Long id) {
        return taskService.getTaskById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(
            value = "/create",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<Task> createTask(
            @RequestPart("task") TaskDTO taskDTO,
            @RequestPart(value = "file", required = false) MultipartFile[] files ,
            @RequestHeader("Authorization") String jwt
    ) {

        Task task = taskService.createTask(taskDTO, jwt);
        if (files != null) {
            for (MultipartFile file : files) {
                taskService.uploadPdf(task.getId(), file, jwt);
            }
        }
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(task);
    }

    @PutMapping("/{id}/update")
    public ResponseEntity<Task> updateTask(
            @PathVariable Long id,
            @RequestPart("task") TaskDTO taskDTO,
            @RequestPart(value = "file", required = false) MultipartFile[] files,
            @RequestHeader("Authorization") String jwt
    ) {
        Task task = taskService.updateTask(id, taskDTO, jwt);
        if (files != null) {

            for (MultipartFile file : files) {
                taskService.uploadPdf(task.getId(), file, jwt);
            }
        }
        return ResponseEntity.ok(task);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id,
                                              @RequestHeader("Authorization") String jwt) {
        taskService.deleteTask(id, jwt);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{taskId}/file/{fileId}")
    public ResponseEntity<String> deleteFile(@PathVariable Long taskId, @PathVariable Long fileId){
        taskService.deleteFile(taskId, fileId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
