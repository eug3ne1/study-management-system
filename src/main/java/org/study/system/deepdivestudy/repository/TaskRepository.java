package org.study.system.deepdivestudy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.study.system.deepdivestudy.entity.task.Task;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByCourse_Id(Long courseId);

}
