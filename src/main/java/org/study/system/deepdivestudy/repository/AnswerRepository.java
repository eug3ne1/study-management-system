package org.study.system.deepdivestudy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.study.system.deepdivestudy.model.testing.Answer;

public interface AnswerRepository extends JpaRepository<Answer,Long> {
}
