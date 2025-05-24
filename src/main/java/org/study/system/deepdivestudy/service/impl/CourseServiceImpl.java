package org.study.system.deepdivestudy.service.impl;


import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.study.system.deepdivestudy.dto.CreateCourseRequest;
import org.study.system.deepdivestudy.dto.UpdateCourseRequest;
import org.study.system.deepdivestudy.entity.Tag;
import org.study.system.deepdivestudy.entity.course.Course;
import org.study.system.deepdivestudy.entity.users.Student;
import org.study.system.deepdivestudy.entity.users.Teacher;
import org.study.system.deepdivestudy.exceptions.AlreadyEnrolledException;
import org.study.system.deepdivestudy.exceptions.CourseNotFoundException;
import org.study.system.deepdivestudy.repository.CourseRepository;
import org.study.system.deepdivestudy.repository.StudentRepository;
import org.study.system.deepdivestudy.repository.TagRepository;
import org.study.system.deepdivestudy.repository.TeacherRepository;
import org.study.system.deepdivestudy.service.CourseService;
import org.study.system.deepdivestudy.service.StudentService;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CourseServiceImpl implements CourseService {
    private final CourseRepository courseRepository;
    private final TeacherRepository teacherRepository;
    private final TagRepository tagRepository;
    private final StudentService studentService;

    @Override
    public Course createCourse(CreateCourseRequest request, Long teacherId) {
        Course course = new Course();
        course.setName(request.getName());
        course.setDescription(request.getDescription());
        course.setTags(request.getTagIds().stream().map(id-> tagRepository.findById(id).
                orElseThrow(() -> new RuntimeException("Tag not found"))).toList());
        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new RuntimeException("Teacher not found"));
        course.setTeacher(teacher);
        return courseRepository.save(course);
    }

    @Override
    public Course getCourseById(Long courseId) {
        return courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));
    }

    @Override
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

    @Override
    public void deleteCourse(Long courseId) {
        courseRepository.deleteById(courseId);
    }

    @Override
    public void enrollStudent(Long courseId, Long studentId) {
        Course course = getCourseById(courseId);
        Student student = studentService.findById(studentId);

        course.getStudents().add(student);
        courseRepository.save(course);
    }

    @Override
    public List<Student> getStudentsInCourse(Long courseId) {
        return courseRepository.findStudentsByCourseId(courseId);
    }

    @Override
    public List<Course> getCoursesByTeacher(Long teacherId) {
        return courseRepository.findByTeacherId(teacherId);
    }

    @Override
    public List<Course> getCoursesByStudent(Long studentId) {
        return courseRepository.findAllByStudents_Id(studentId);
    }

    @Override
    public List<Course> getAllCourses() {
        return (List<Course>) courseRepository.findAll();
    }

    @Override
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

    @Override
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

        course.getStudents().remove(student);
//        student.getEnrollments().remove(course);
        courseRepository.save(course);
    }

    @Override
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
