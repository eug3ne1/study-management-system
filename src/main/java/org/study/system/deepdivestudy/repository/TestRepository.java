package org.study.system.deepdivestudy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.study.system.deepdivestudy.entity.course.Course;
import org.study.system.deepdivestudy.entity.testing.Test;

import java.util.List;

public interface TestRepository extends JpaRepository<Test,Long> {

    List<Test> getTestsByCourse(Course course);

    List<Test> getTestsById(Long id);
}
