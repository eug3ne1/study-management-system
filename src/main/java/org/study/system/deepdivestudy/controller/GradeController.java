package org.study.system.deepdivestudy.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.study.system.deepdivestudy.dto.TaskGradeDTO;
import org.study.system.deepdivestudy.dto.TestGradeDTO;
import org.study.system.deepdivestudy.entity.users.Student;
import org.study.system.deepdivestudy.service.GradeService;
import org.study.system.deepdivestudy.service.StudentService;

import java.util.List;
@RestController
@RequestMapping("/api/grades")
@AllArgsConstructor
public class GradeController {

    private final GradeService gradeService;
    private final StudentService studentService;



    @GetMapping("/test/{courseId}")
    public List<TestGradeDTO> getAllTestsGradesByCourse(@PathVariable Long courseId){
        return gradeService.getAllTestGradesByCourse(courseId);
    }

    @GetMapping("/task/{courseId}")
    public List<TaskGradeDTO> getAllTasksGradesByCourse(@PathVariable Long courseId){
        return gradeService.getAllTaskGradesByCourse(courseId);
    }

    @GetMapping("tests/course/{courseId}/student")
    public ResponseEntity<List<TestGradeDTO>> getStudentTestGradesByCourse(@PathVariable Long courseId, @RequestHeader("Authorization") String jwt){
        Student student = studentService.getStudentByJWT(jwt);
        return new ResponseEntity<>(gradeService.getStudentTestGrades(courseId, student.getId()), HttpStatus.OK);
    }

    @GetMapping("tasks/course/{courseId}/student")
    public ResponseEntity<List<TaskGradeDTO>> getStudentTaskGradesByCourse(@PathVariable Long courseId, @RequestHeader("Authorization") String jwt){
        Student student = studentService.getStudentByJWT(jwt);
        return new ResponseEntity<>(gradeService.getStudentTaskGrades(courseId, student.getId()), HttpStatus.OK);
    }










}
