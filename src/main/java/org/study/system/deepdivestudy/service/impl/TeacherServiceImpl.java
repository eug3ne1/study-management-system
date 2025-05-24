package org.study.system.deepdivestudy.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.study.system.deepdivestudy.dto.TeacherDTO;
import org.study.system.deepdivestudy.exceptions.TeacherNotFoundException;
import org.study.system.deepdivestudy.entity.users.Teacher;
import org.study.system.deepdivestudy.entity.users.User;
import org.study.system.deepdivestudy.repository.TeacherRepository;
import org.study.system.deepdivestudy.service.TeacherService;
import org.study.system.deepdivestudy.service.UserService;


import java.util.ArrayList;
import java.util.List;


@Service
@AllArgsConstructor
public class TeacherServiceImpl implements TeacherService {
    private TeacherRepository teacherRepository;
    private UserService userService;

    @Override
    public Teacher getTeacherByJWT(String jwt){
        User userByJWT = userService.getUserByJWT(jwt);
        return teacherRepository.findByUser(userByJWT);
    }

    @Override
    public Teacher findById(Long id){
        return teacherRepository.findById(id).orElseThrow(
                ()-> new TeacherNotFoundException("Teacher not found with such id"));
    }

    @Override
    public List<TeacherDTO> getTeachersByUni(Long id) {
        List<Teacher> teacherList = teacherRepository.findAllByUniversityId(id);
        List<TeacherDTO> teacherDTOList = new ArrayList<>();
        for (Teacher teacher : teacherList) {
            TeacherDTO teacherDTO = new TeacherDTO(teacher.getId(), teacher.getFirstName(),
                    teacher.getLastName(),teacher.getMiddleName());
            teacherDTOList.add(teacherDTO);
        }

        return teacherDTOList;

    }

}
