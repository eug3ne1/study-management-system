package org.study.system.deepdivestudy.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.study.system.deepdivestudy.entity.Tag;
import org.study.system.deepdivestudy.entity.course.Course;
import org.study.system.deepdivestudy.entity.users.Student;
import org.study.system.deepdivestudy.entity.users.Teacher;

import java.util.List;

public interface CourseRepository extends CrudRepository<Course,Long> {

    List<Course> findByTeacherId(Long teacherId);

    @Query("SELECT c.students FROM Course c WHERE c.id = :courseId")
    List<Student> findStudentsByCourseId(@Param("courseId") Long courseId);

    List<Course> findDistinctByTags(List<Tag> tags);

    List<Course> findAllByStudents_Id(Long studentId);
    
    Integer countCoursesByTeacher(Teacher teacher);


    @Query("""
    SELECT COUNT(DISTINCT s.id)
      FROM Course c
      JOIN c.students s
     WHERE c.teacher.id = :teacherId
""")
    Integer countStudentsByTeacher(@Param("teacherId") Long teacherId);


    @Query("""
    SELECT COUNT(DISTINCT t.id)
      FROM Course c
      JOIN c.tests t
     WHERE c.teacher.id = :teacherId AND t.endTime > CURRENT_TIMESTAMP
""")
    Integer countTestsByTeacher(@Param("teacherId") Long teacherId);



}
