package org.study.system.deepdivestudy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.study.system.deepdivestudy.entity.course.Lecture;

import java.util.List;

public interface LectureRepository extends JpaRepository<Lecture,Long> {

    List<Lecture> findByCourse_Id(Long courseId);

    Long id(Long id);
}
