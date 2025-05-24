package org.study.system.deepdivestudy.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import org.study.system.deepdivestudy.exceptions.AlreadyEnrolledException;
import org.study.system.deepdivestudy.exceptions.CourseNotFoundException;
import org.study.system.deepdivestudy.entity.course.Course;

import org.study.system.deepdivestudy.entity.users.Student;
import org.study.system.deepdivestudy.entity.users.User;
import org.study.system.deepdivestudy.repository.*;

import java.util.Optional;

@Service
@AllArgsConstructor
public class StudentService {

    private StudentRepository studentRepository;
    private CourseRepository courseRepository;
    private UserService userService;



    public Student getStudentByJWT(String jwt){
        User userByJWT = userService.getUserByJWT(jwt);
        return studentRepository.findByUser(userByJWT);
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

}
