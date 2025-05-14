package org.study.system.deepdivestudy.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.study.system.deepdivestudy.dto.*;
import org.study.system.deepdivestudy.entity.testing.Test;
import org.study.system.deepdivestudy.entity.users.Student;
import org.study.system.deepdivestudy.service.StudentService;
import org.study.system.deepdivestudy.service.StudentTestService;
import org.study.system.deepdivestudy.service.TestService;

import java.util.List;

@RestController

@RequestMapping("/api/tests")
@RequiredArgsConstructor
public class TestController {
    private final TestService testService;
    private final StudentService studentService;
    private final StudentTestService studentTestService;


    @GetMapping("/{id}")
    public ResponseEntity<TestResponse> getById(@PathVariable Long id){
        System.out.println(id);
        TestResponse byId = testService.getById(id);
        return new ResponseEntity<>(byId,HttpStatus.OK);
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<TestResponse>> getByCourseId(@PathVariable Long courseId){
        List<TestResponse> tests = testService.getTestsByCourseId(courseId);
        return new ResponseEntity<>(tests,HttpStatus.OK);
    }

    @PostMapping("/create-full")
    public ResponseEntity<TestResponse> createFullTest(@RequestBody TestDTO request,
                                                       @RequestHeader("Authorization") String jwt) {
        TestResponse createdTest = testService.createTestFromRequest(request, jwt);
        return new ResponseEntity<>(createdTest, HttpStatus.CREATED);
    }


    @PostMapping("/{testId}/add-full-question")
    public ResponseEntity<QuestionResponse> addFullQuestion(
            @PathVariable Long testId,
            @RequestBody QuestionCreateRequest request,
            @RequestHeader("Authorization") String jwt
    ) {
        QuestionResponse question = testService.addFullQuestion(testId, request, jwt);
        return new ResponseEntity<>(question, HttpStatus.CREATED);
    }

    @PutMapping("/update/{testId}")
    public ResponseEntity<TestResponse> updateTest(@PathVariable Long testId, @RequestBody TestDTO dto,
                                             @RequestHeader("Authorization") String jwt){
        TestResponse testResponse = testService.updateTest(testId, dto, jwt);
        return new ResponseEntity<>(testResponse, HttpStatus.OK);

    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable  Long id){
        testService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }



    @GetMapping ("{testId}/attempts")
    public ResponseEntity<Integer> getStudentAttempts(@PathVariable  Long testId,
                                                        @RequestHeader("Authorization") String jwt){
        Student student = studentService.getStudentByJWT(jwt);
        return new ResponseEntity<>(studentTestService.countStudentAttempts(testId, student),HttpStatus.OK);

    }








}