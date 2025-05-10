package org.study.system.deepdivestudy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.study.system.deepdivestudy.model.course.Course;
import org.study.system.deepdivestudy.model.testing.Test;

import java.util.List;

public interface TestRepository extends JpaRepository<Test,Long> {

    List<Test> getTestsByCourse(Course course);

    List<Test> getTestsById(Long id);
}
