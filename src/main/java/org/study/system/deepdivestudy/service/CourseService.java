package org.study.system.deepdivestudy.service;

import org.study.system.deepdivestudy.dto.CreateCourseRequest;
import org.study.system.deepdivestudy.dto.UpdateCourseRequest;
import org.study.system.deepdivestudy.entity.course.Course;
import org.study.system.deepdivestudy.entity.users.Student;

import java.util.List;

public interface CourseService {

    Course createCourse(CreateCourseRequest request, Long teacherId);

    Course getCourseById(Long courseId);

    Course updateCourse(Long courseId, UpdateCourseRequest request);

    void deleteCourse(Long courseId);

    void enrollStudent(Long courseId, Long studentId);

    List<Student> getStudentsInCourse(Long courseId);

    Course addStudentToCourse(Long courseId, Student student);

    void deleteStudentCourse( Long courseId, String jwt);

    List<Course> findByTags(List<String> tags);

    List<Course> getCoursesByTeacher(Long teacherId);

    List<Course> getCoursesByStudent(Long studentId);

    List<Course> getAllCourses();




}
