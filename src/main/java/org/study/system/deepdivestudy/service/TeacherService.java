package org.study.system.deepdivestudy.service;

import org.study.system.deepdivestudy.dto.TeacherDTO;
import org.study.system.deepdivestudy.entity.users.Teacher;

import java.util.List;

public interface TeacherService {
    Teacher getTeacherByJWT(String jwt);

    Teacher findById(Long id);

    List<TeacherDTO> getTeachersByUni(Long id);
}
