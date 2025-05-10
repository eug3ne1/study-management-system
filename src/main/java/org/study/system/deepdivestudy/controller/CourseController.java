package org.study.system.deepdivestudy.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.study.system.deepdivestudy.dto.CourseDTO;
import org.study.system.deepdivestudy.dto.CreateCourseRequest;
import org.study.system.deepdivestudy.dto.UpdateCourseRequest;
import org.study.system.deepdivestudy.exceptions.TeacherNotFoundException;
import org.study.system.deepdivestudy.model.University;
import org.study.system.deepdivestudy.model.course.Course;
import org.study.system.deepdivestudy.model.users.Student;
import org.study.system.deepdivestudy.model.users.Teacher;
import org.study.system.deepdivestudy.repository.UserRepository;
import org.study.system.deepdivestudy.service.CourseService;
import org.study.system.deepdivestudy.service.TeacherService;
import org.study.system.deepdivestudy.service.UniversityService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/courses")
@AllArgsConstructor
public class CourseController {

    private final CourseService courseService;
    private final TeacherService teacherService;
    private final UniversityService universityService;



    @PostMapping("/create")
    public CourseDTO createCourse(@RequestBody CreateCourseRequest request,
                                  @RequestHeader("Authorization") String jwt) {
        Teacher teacherByJWT = teacherService.getTeacherByJWT(jwt);
        University university = universityService.getById(request.getUniId()).orElseThrow(IllegalStateException::new);
        university.getTeachers().add(teacherByJWT);

        return CourseDTO.fromEntity(courseService.createCourse(request, teacherByJWT.getId()));
    }

    @GetMapping("/{courseId}")
    public CourseDTO getCourse(@PathVariable Long courseId) {
        return CourseDTO.fromEntity(courseService.getCourseById(courseId));
    }

    @PutMapping("/{courseId}")
    public CourseDTO updateCourse(@PathVariable Long courseId, @RequestBody UpdateCourseRequest request) {
        Course updatedCourse = courseService.updateCourse(courseId, request);
        return CourseDTO.fromEntity(updatedCourse);
    }

    @DeleteMapping("/{courseId}")
    public void deleteCourse(@PathVariable Long courseId) {
        courseService.deleteCourse(courseId);
    }

    @PostMapping("/{courseId}/enroll/{studentId}")
    public void enrollStudent(@PathVariable Long courseId, @PathVariable Long studentId) {
        courseService.enrollStudent(courseId, studentId);
    }

    @GetMapping("/{courseId}/students")
    public List<Student> getStudentsInCourse(@PathVariable Long courseId) {
        return courseService.getStudentsInCourse(courseId);
    }

    @GetMapping("/teacher")
    public List<CourseDTO> getCoursesByTeacher( @RequestHeader("Authorization") String jwt) {
        Teacher teacherByJWT = teacherService.getTeacherByJWT(jwt);
        if (teacherByJWT==null){
            throw new TeacherNotFoundException("Teacher not found");
        }
        return courseService.getCoursesByTeacher(teacherByJWT.getId())
                .stream()
                .map(CourseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @GetMapping("/teacher/{teacherId}")
    public List<CourseDTO> getCoursesByTeacher(@PathVariable Long teacherId) {
        return courseService.getCoursesByTeacher(teacherId)
                .stream()
                .map(CourseDTO::fromEntity)
                .collect(Collectors.toList());
    }


    @GetMapping("/search")
    public List<Course> getCoursesByTags(@RequestParam("tags") List<String> tags) {
        return courseService.findByTags(tags);
    }

    @GetMapping
    public List<CourseDTO> getAllCourses() {
        return courseService.getAllCourses()
                .stream()
                .map(CourseDTO::fromEntity)
                .collect(Collectors.toList());
    }


    @DeleteMapping("/student/{courseId}")
    public ResponseEntity<String> deleteByid(@PathVariable Long courseId,
                                            @RequestHeader("Authorization") String jwt){

        courseService.deleteStudentCourse(courseId,jwt);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
