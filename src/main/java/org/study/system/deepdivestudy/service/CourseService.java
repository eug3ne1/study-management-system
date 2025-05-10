package org.study.system.deepdivestudy.service;


import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.study.system.deepdivestudy.dto.CreateCourseRequest;
import org.study.system.deepdivestudy.dto.UpdateCourseRequest;
import org.study.system.deepdivestudy.model.Tag;
import org.study.system.deepdivestudy.model.course.Course;
import org.study.system.deepdivestudy.model.users.Student;
import org.study.system.deepdivestudy.model.users.Teacher;
import org.study.system.deepdivestudy.repository.CourseRepository;
import org.study.system.deepdivestudy.repository.StudentRepository;
import org.study.system.deepdivestudy.repository.TagRepository;
import org.study.system.deepdivestudy.repository.TeacherRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class CourseService {
    private final CourseRepository courseRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final TagRepository tagRepository;
    private final StudentService studentService;




    public Course createCourse(CreateCourseRequest request, Long teacherId) {
        Course course = new Course();
        course.setName(request.getName());
        course.setDescription(request.getDescription());
        course.setTags(request.getTagIds().stream().map(id-> {
            return tagRepository.findById(id).orElseThrow(() -> new RuntimeException("Tag not found"));
        }).toList());
        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new RuntimeException("Teacher not found"));
        course.setTeacher(teacher);
        return courseRepository.save(course);
    }


    public Course getCourseById(Long courseId) {
        return courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));
    }


    public Course updateCourse(Long courseId, UpdateCourseRequest request) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        course.setName(request.getName());
        course.setDescription(request.getDescription());
        course.setTags(request.getTagIds().stream().map(id-> {
            return tagRepository.findById(id).orElseThrow(() -> new RuntimeException("Tag not found"));
        }).toList());

        return courseRepository.save(course);
    }


    public void deleteCourse(Long courseId) {
        courseRepository.deleteById(courseId);
    }


    public void enrollStudent(Long courseId, Long studentId) {
        Course course = getCourseById(courseId);
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        course.getStudents().add(student);
        courseRepository.save(course);
    }


    public List<Student> getStudentsInCourse(Long courseId) {
        return courseRepository.findStudentsByCourseId(courseId);
    }


    public List<Course> getCoursesByTeacher(Long teacherId) {
        return courseRepository.findByTeacherId(teacherId);
    }

    public List<Course> getCoursesByStudent(Long studentId) {
        return courseRepository.findAllByStudents_Id(studentId);
    }


    public List<Course> getAllCourses() {
        return (List<Course>) courseRepository.findAll();
    }


    public List<Course> findByTags(List<String> tags) {
        List<Tag> tagList = tags.stream().map(tagName -> {
            Tag tag = new Tag();
            tag.setName(tagName);
            return tag;
        }).toList();
        List<Course> distinctByTags = courseRepository.findDistinctByTags(tagList);
        if(distinctByTags== null){
            throw new RuntimeException("No courses with such tags");
        }
        return distinctByTags;
    }

    @Transactional
    public void deleteStudentCourse( Long courseId, String jwt) {

        Student student = studentService.getStudentByJWT(jwt);

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("Course %d not found".formatted(courseId)));

        // Safety-check: was the student really enrolled?
        if (!course.getStudents().contains(student)) {
            throw new IllegalStateException(
                    "Student %d is not enrolled in course %d".formatted(student.getId(), courseId));
        }

        /*  ----  OWN-SIDE update (Course -> Student)  ----  */
        course.getStudents().remove(student);        // owning side
        student.getEnrollments().remove(course);
        courseRepository.save(course);
        studentRepository.save(student);
    }
}
