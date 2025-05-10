package org.study.system.deepdivestudy.service;

import org.springframework.stereotype.Service;

import org.study.system.deepdivestudy.exceptions.AlreadyEnrolledException;
import org.study.system.deepdivestudy.exceptions.CourseNotFoundException;
import org.study.system.deepdivestudy.model.course.Course;

import org.study.system.deepdivestudy.model.users.Student;
import org.study.system.deepdivestudy.model.users.User;
import org.study.system.deepdivestudy.repository.*;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    private StudentRepository studentRepository;
    private CourseRepository courseRepository;
    private UniversityRepository universityRepository;
    private TestRepository testRepository;
    private TestGradeRepository gradeRepository;
    private UserService userService;

    public StudentService(CourseRepository courseRepository, TestGradeRepository gradeRepository,
                          StudentRepository studentRepository, TestRepository testRepository,
                          UniversityRepository universityRepository, UserService userService) {
        this.courseRepository = courseRepository;
        this.gradeRepository = gradeRepository;
        this.studentRepository = studentRepository;
        this.testRepository = testRepository;
        this.universityRepository = universityRepository;
        this.userService = userService;
    }

    public Student getStudentByJWT(String jwt){
        User userByJWT = userService.getUserByJWT(jwt);
        return studentRepository.findByUser(userByJWT);
    }

    public List<Student> getAllStudents(){
        return studentRepository.findAll();
    }
    public Course addStudentToCourse(Long courseId, Student student){
        Optional<Course> CourseById = courseRepository.findById(courseId);
        if(CourseById.isPresent()){
            Course course = CourseById.get();
            if(course.getStudents().contains(student)){
                throw new AlreadyEnrolledException("User has already enrolled the course");
            }
            course.getStudents().add(student);
            return courseRepository.save(course);
        }else {
            throw new CourseNotFoundException("Course not found with id"+courseId);
        }

    }
    public void removeStudentFromCourse(Course course, Student student){
        course.getStudents().remove(student);
        courseRepository.save(course);
    }
}
