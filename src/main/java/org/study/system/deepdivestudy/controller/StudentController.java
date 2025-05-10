package org.study.system.deepdivestudy.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.study.system.deepdivestudy.dto.*;
import org.study.system.deepdivestudy.exceptions.StudentNotFoundException;
import org.study.system.deepdivestudy.model.course.Course;
import org.study.system.deepdivestudy.model.course.Lecture;
import org.study.system.deepdivestudy.model.users.Student;
import org.study.system.deepdivestudy.service.CourseService;
import org.study.system.deepdivestudy.service.GradeService;
import org.study.system.deepdivestudy.service.StudentService;
import org.study.system.deepdivestudy.service.StudentTestService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/student")
@AllArgsConstructor
public class StudentController {

    private final StudentService studentService;
    private final CourseService courseService;
    private final StudentTestService studentTestService;
    private final GradeService gradeService;



//    @GetMapping("/{courseId}")
//    public List<CourseGradeResponse> getAllStudentGradesByCourse(@PathVariable Long courseId,
//                                                                 @RequestHeader("Authorization") String jwt){
//        Student student = studentService.getStudentByJWT(jwt);
//        return gradeService.getGradesByStudentCourse(student, courseId);
//    }

    @GetMapping("/courses")
    public ResponseEntity<List<CourseDTO>> getAllStudentCourses(@RequestHeader("Authorization") String jwt){
        Student studentByJWT = studentService.getStudentByJWT(jwt);

        return new ResponseEntity<>(courseService.getCoursesByStudent(studentByJWT.getId())
                .stream()
                .map(CourseDTO::fromEntity)
                .collect(Collectors.toList()),HttpStatus.OK);

    }


    @PostMapping("/courses/enroll/{courseId}")
    public ResponseEntity<UpdateCourseRequest> enroll(@PathVariable Long courseId, @RequestHeader("Authorization") String jwt){
        Student student = studentService.getStudentByJWT(jwt);
        Course course = studentService.addStudentToCourse(courseId, student);
        UpdateCourseRequest updateCourseRequest = new UpdateCourseRequest();
        updateCourseRequest.setName(course.getName());
        updateCourseRequest.setDescription(course.getDescription());
        return new ResponseEntity<>(updateCourseRequest, HttpStatus.OK);
    }

    @GetMapping("/{courseId}/lectures")
    public List<Lecture> getAllLecturesInCourse(@PathVariable Long courseId, @RequestHeader("Authorization") String jwt){
        Course course = courseService.getCourseById(courseId);
        Student student = studentService.getStudentByJWT(jwt);
        if(course.getStudents().contains(student)){
            return course.getLectures();
        }else {
            throw new StudentNotFoundException("No such student for this course");
        }
    }

    @PostMapping("test/submit")
    public ResponseEntity<TestResultResponse> submitTest(@RequestBody TestSubmissionRequest request,
                                                         @RequestHeader("Authorization") String jwt) {
        TestResultResponse result = studentTestService.evaluateTest(request, jwt);
        return ResponseEntity.ok(result);
    }







}

