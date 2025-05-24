package org.study.system.deepdivestudy.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.study.system.deepdivestudy.entity.users.Student;
import org.study.system.deepdivestudy.entity.users.User;
import org.study.system.deepdivestudy.exceptions.StudentNotFoundException;
import org.study.system.deepdivestudy.repository.*;
import org.study.system.deepdivestudy.service.StudentService;
import org.study.system.deepdivestudy.service.UserService;


@Service
@AllArgsConstructor
public class StudentServiceImpl implements StudentService {

    private StudentRepository studentRepository;
    private CourseRepository courseRepository;
    private UserService userService;


    @Override
    public Student getStudentByJWT(String jwt){
        User userByJWT = userService.getUserByJWT(jwt);
        return studentRepository.findByUser(userByJWT);
    }

    @Override
    public Student findById(Long studentId) {
        return studentRepository.findById(studentId)
                .orElseThrow(()-> new StudentNotFoundException("Student not found with such id"));
    }
}
