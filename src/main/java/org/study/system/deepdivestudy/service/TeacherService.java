package org.study.system.deepdivestudy.service;

import org.springframework.stereotype.Service;
import org.study.system.deepdivestudy.dto.TeacherDTO;
import org.study.system.deepdivestudy.exceptions.TeacherNotFoundException;
import org.study.system.deepdivestudy.model.users.Teacher;
import org.study.system.deepdivestudy.model.users.User;
import org.study.system.deepdivestudy.repository.TeacherRepository;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TeacherService {
    private TeacherRepository teacherRepository;
    private UserService userService;

    public TeacherService(TeacherRepository teacherRepository, UserService userService) {
        this.teacherRepository = teacherRepository;
        this.userService = userService;
    }

    public Teacher getTeacherByJWT(String jwt){
        User userByJWT = userService.getUserByJWT(jwt);
        return teacherRepository.findByUser(userByJWT);
    }

    public Teacher findById(Long id){
        return teacherRepository.findById(id).orElseThrow(
                ()-> new TeacherNotFoundException("Teacher not found with such id"));
    }

    public List<TeacherDTO> getTeachersByUni(Long id) {
        List<Teacher> teacherList = teacherRepository.findAllByUniversityId(id);
        List<TeacherDTO> teacherDTOList = new ArrayList<>();
        for (Teacher teacher : teacherList) {
            TeacherDTO teacherDTO = new TeacherDTO(teacher.getId(), teacher.getFirstName(), teacher.getLastName(),teacher.getMiddleName());
            teacherDTOList.add(teacherDTO);
        }

        return teacherDTOList;

    }

}
