package org.study.system.deepdivestudy.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.study.system.deepdivestudy.model.testing.*;
import org.study.system.deepdivestudy.model.users.Student;
import org.study.system.deepdivestudy.repository.*;
import org.study.system.deepdivestudy.dto.TestResultResponse;
import org.study.system.deepdivestudy.dto.TestSubmissionRequest;


import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentTestService {

    private final TestRepository testRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final TestGradeRepository gradeRepository;
    private final StudentService studentService;
    private final TestAttemptRepository attemptRepository;

    public TestResultResponse evaluateTest(TestSubmissionRequest request, String jwt) {
        Test test = testRepository.findById(request.getTestId())
                .orElseThrow(() -> new RuntimeException("Test not found"));
        LocalDateTime now = LocalDateTime.now();

        Student student = studentService.getStudentByJWT(jwt);

        if (now.isBefore(test.getStartTime())) {
            throw new IllegalStateException("Тест ще не почався.");
        }

        if (now.isAfter(test.getEndTime())) {
            throw new IllegalStateException("Час на проходження тесту вичерпано.");
        }

        Integer attemptsByStudent = attemptRepository.countTestAttemptsByStudentIdAndTestId(student.getId(), test.getId());
        if (attemptsByStudent == null){
            attemptsByStudent = 0;
        }
        if (test.getMaxAttempts() != null && attemptsByStudent >= test.getMaxAttempts()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ви вичерпали всі спроби");
        }


        double totalScore = 0;
        double maxScore = 0;
        int correctCount = 0;

        for (TestSubmissionRequest.SubmittedAnswer submitted : request.getAnswers()) {
            Question question = questionRepository.findById(submitted.getQuestionId())
                    .orElseThrow(() -> new RuntimeException("Question not found"));

            maxScore += question.getValue();

            List<Long> correctIds = question.getAnswers().stream()
                    .filter(Answer::getIsCorrect)
                    .map(Answer::getId)
                    .toList();

            List<Long> selectedIds = submitted.getSelectedAnswerIds();

            boolean isCorrect = new HashSet<>(correctIds).equals(new HashSet<>(selectedIds));

            if (isCorrect) {
                totalScore += question.getValue();
                correctCount++;
            }
        }

        saveGrade(student,test,totalScore);
        saveAttempt(student,test,totalScore);

        // Формуємо відповідь
        TestResultResponse response = new TestResultResponse();
        response.setTestId(test.getId());
        response.setTotalQuestions(test.getQuestions().size());
        response.setCorrectAnswers(correctCount);
        response.setTotalScore(totalScore);
        response.setMaxScore(maxScore);
        response.setGrade(totalScore);
        response.setAttempt(attemptsByStudent+1);

        return response;
    }

    private void saveAttempt(Student student, Test test, double totalScore) {
        TestAttempt attempt = new TestAttempt();
        attempt.setStudent(student);
        attempt.setTest(test);
        attempt.setScore(totalScore);
        attempt.setSubmittedAt(LocalDateTime.now());
        attemptRepository.save(attempt);
    }

    private void saveGrade(Student student, Test test, Double score){
        // Зберігаємо оцінку
        // Оновлюємо оцінку, якщо це найкраща спроба
        TestGrade testGrade = gradeRepository.findByStudentIdAndTestId(student.getId(), test.getId())
                .orElse(null);

        if (testGrade == null) {
            testGrade = new TestGrade();
            testGrade.setStudent(student);
            testGrade.setTest(test);
            testGrade.setScore(score);
            gradeRepository.save(testGrade);
        } else if (score > testGrade.getScore()) {
            testGrade.setScore(score);
            gradeRepository.save(testGrade);
        }
    }

    public Integer countStudentAttempts(Long testId, Student student) {
        return attemptRepository.countTestAttemptsByStudentIdAndTestId(student.getId(),testId);
    }
}

