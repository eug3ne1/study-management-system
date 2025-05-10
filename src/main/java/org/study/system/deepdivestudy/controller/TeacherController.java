package org.study.system.deepdivestudy.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.study.system.deepdivestudy.model.users.Teacher;
import org.study.system.deepdivestudy.repository.CourseRepository;
import org.study.system.deepdivestudy.repository.TestRepository;
import org.study.system.deepdivestudy.service.TeacherService;

@RestController()
@RequestMapping("api/teacher")
@AllArgsConstructor
public class TeacherController {
    TeacherService teacherService;
    CourseRepository courseRepository;


    @GetMapping("/{id}")
    public ResponseEntity<Teacher> getById(@PathVariable  Long id){
        return new ResponseEntity<>(teacherService.findById(id), HttpStatus.OK);
    }


    @GetMapping("/courses/count")
    public ResponseEntity<Integer> countCourses(@RequestHeader("Authorization") String jwt){
        Teacher teacher = teacherService.getTeacherByJWT(jwt);
        return new ResponseEntity<>(courseRepository.countCoursesByTeacher(teacher),HttpStatus.OK);
    }

    @GetMapping("/tests/count")
    public ResponseEntity<Integer> countTests(@RequestHeader("Authorization") String jwt){
        Teacher teacher = teacherService.getTeacherByJWT(jwt);
        return new ResponseEntity<>(courseRepository.countTestsByTeacher(teacher.getId()),HttpStatus.OK);
    }

    @GetMapping("/students/count")
    public ResponseEntity<Integer> countStudents(@RequestHeader("Authorization") String jwt){
        Teacher teacher = teacherService.getTeacherByJWT(jwt);
        return new ResponseEntity<>(courseRepository.countStudentsByTeacher(teacher.getId()),HttpStatus.OK);
    }

}
