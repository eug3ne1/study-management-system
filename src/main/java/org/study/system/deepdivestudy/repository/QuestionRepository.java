package org.study.system.deepdivestudy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.study.system.deepdivestudy.entity.testing.Question;

public interface QuestionRepository extends JpaRepository<Question, Long> {

}
