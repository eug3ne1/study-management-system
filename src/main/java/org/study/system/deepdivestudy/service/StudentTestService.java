package org.study.system.deepdivestudy.service;

import org.study.system.deepdivestudy.dto.TestResultResponse;
import org.study.system.deepdivestudy.dto.TestSubmissionRequest;
import org.study.system.deepdivestudy.entity.testing.Test;
import org.study.system.deepdivestudy.entity.users.Student;

public interface StudentTestService {
    TestResultResponse evaluateTest(TestSubmissionRequest request, String jwt);

    void saveAttempt(Student student, Test test, double totalScore);

    void saveGrade(Student student, Test test, Double score);

    Integer countStudentAttempts(Long testId, Student student);
}
