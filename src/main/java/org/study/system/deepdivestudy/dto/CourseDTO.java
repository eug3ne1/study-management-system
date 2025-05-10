package org.study.system.deepdivestudy.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.study.system.deepdivestudy.model.Tag;
import org.study.system.deepdivestudy.model.course.Course;
import org.study.system.deepdivestudy.model.users.Student;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseDTO {

    private Long id;
    private String name;
    private String description;
    private Long teacherId;
    private List<Long> studentIds;
    private List<Tag> tags;
    private TeacherDTO teacherDTO;


    public static CourseDTO fromEntity(Course course) {
        List<Long> students = course.getStudents().stream()
                .map(Student::getId)
                .collect(Collectors.toList());

        return new CourseDTO(
                course.getId(),
                course.getName(),
                course.getDescription(),
                course.getTeacher() != null ? course.getTeacher().getId() : null,
                students,
                course.getTags(),
                new TeacherDTO(course.getTeacher().getId(),
                        course.getTeacher().getFirstName(),
                        course.getTeacher().getLastName(),
                        course.getTeacher().getMiddleName()
                ));
    }
}

