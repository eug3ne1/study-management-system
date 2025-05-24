package org.study.system.deepdivestudy.service;

import org.study.system.deepdivestudy.entity.users.Student;

public interface StudentService {

    Student getStudentByJWT(String jwt);

    Student findById(Long studentId);
}
