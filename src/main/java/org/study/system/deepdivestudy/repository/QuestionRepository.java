package org.study.system.deepdivestudy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.study.system.deepdivestudy.entity.testing.Question;
import org.study.system.deepdivestudy.entity.testing.Test;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    List<Question> getQuestionsByTest(Test test);
}
