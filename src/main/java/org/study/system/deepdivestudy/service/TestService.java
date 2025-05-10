package org.study.system.deepdivestudy.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.study.system.deepdivestudy.dto.*;
import org.study.system.deepdivestudy.exceptions.ResourceNotFoundException;
import org.study.system.deepdivestudy.exceptions.TestNotFoundException;
import org.study.system.deepdivestudy.model.course.Course;
import org.study.system.deepdivestudy.model.testing.Answer;
import org.study.system.deepdivestudy.model.testing.Question;
import org.study.system.deepdivestudy.model.testing.Test;
import org.study.system.deepdivestudy.model.users.Student;
import org.study.system.deepdivestudy.model.users.Teacher;
import org.study.system.deepdivestudy.model.users.User;
import org.study.system.deepdivestudy.repository.*;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TestService {
    private final TestRepository testRepository;
    private final AnswerRepository answerRepository;
    private final LectureRepository lectureRepository;
    private final CourseService courseService;
    private final UserService userService;
    private final QuestionRepository questionRepository;
    private final TeacherService teacherService;

    public Test createEmptyTest(EmptyTestCreateRequest request, String jwt) {
        authenticateTeacherByCourse(request.getCourseId(), jwt);
        Test test = new Test();
        test.setName(request.getName());
        test.setDescription(request.getDescription());
        test.setMaxAttempts(request.getMaxAttempts());
        test.setStartTime(request.getStartTime());
        test.setEndTime(request.getEndTime());
        test.setCourse(courseService.getCourseById(request.getCourseId()));
        if (request.getLectureId()!=null){
            test.setLecture(lectureRepository.findById(request.getLectureId()).orElseThrow());
        }

        return testRepository.save(test);

    }

    public TestResponse getById(Long id) {
        Test test = testRepository.findById(id).orElseThrow(() -> new TestNotFoundException("Test not found"));
        return TestMapper.toDto(test);
    }

    public TestResponse createTestFromRequest(TestDTO request, String jwt) {
        authenticateTeacherByCourse(request.getCourseId(), jwt);
        Test test = new Test();
        test.setName(request.getName());
        test.setDescription(request.getDescription());
        test.setMaxAttempts(request.getMaxAttempts());
        test.setStartTime(request.getStartTime());
        test.setEndTime(request.getEndTime());




        List<Question> questions = new ArrayList<>();

        for (QuestionCreateRequest qRequest : request.getQuestions()) {
            Question question = new Question();
            question.setText(qRequest.getText());
            question.setContent(qRequest.getContent());
            question.setValue(qRequest.getValue());
            question.setType(qRequest.getType());
            question.setTest(test);
            test.setScore(test.getScore()+qRequest.getValue());

            List<Answer> answers = new ArrayList<>();
            for (AnswerCreateRequest aRequest : qRequest.getAnswers()) {
                Answer answer = new Answer();
                answer.setText(aRequest.getText());
                answer.setIsCorrect(aRequest.getIsCorrect());
                answer.setQuestion(question);
                answers.add(answer);
            }
            question.setAnswers(answers);
            questions.add(question);
        }

        test.setQuestions(questions);
        Course course = courseService.getCourseById(request.getCourseId());
        test.setCourse(course);
        Test savedTest = testRepository.save(test);

        return TestMapper.toDto(savedTest);
    }

    public TestResponse updateTest(Long testId, TestDTO request, String jwt) {

        authenticateTeacherByCourse(request.getCourseId(), jwt);

        Test test = testRepository.findById(testId)
                .orElseThrow(() -> new TestNotFoundException("Test not found"));


        test.setName(request.getName());
        test.setDescription(request.getDescription());
        test.setMaxAttempts(request.getMaxAttempts());
        test.setStartTime(request.getStartTime());
        test.setEndTime(request.getEndTime());


        test.getQuestions().clear();
        test.setScore(0d);

        // Створити нові питання
        List<Question> newQuestions = new ArrayList<>();
        for (QuestionCreateRequest qReq : request.getQuestions()) {
            Question q = new Question();
            q.setText(qReq.getText());
            q.setContent(qReq.getContent());
            q.setValue(qReq.getValue());
            q.setType(qReq.getType());
            q.setTest(test);

            test.setScore(test.getScore() + qReq.getValue());

            List<Answer> ansList = new ArrayList<>();
            for (AnswerCreateRequest aReq : qReq.getAnswers()) {
                Answer a = new Answer();
                a.setText(aReq.getText());
                a.setIsCorrect(aReq.getIsCorrect());
                a.setQuestion(q);
                ansList.add(a);
            }

            q.getAnswers().addAll(ansList);
            newQuestions.add(q);
        }
        test.getQuestions().addAll(newQuestions);

        // Зберегти оновлений тест
        Test saved = testRepository.save(test);
        return TestMapper.toDto(saved);
    }


    public QuestionResponse addFullQuestion(Long testId, QuestionCreateRequest request, String jwt) {
        User teacher = userService.getUserByJWT(jwt);
        Test test = testRepository.findById(testId)
                .orElseThrow(() -> new ResourceNotFoundException("Test not found"));

        if (!test.getCourse().getTeacher().getId().equals(teacher.getId())) {
            throw new AccessDeniedException("You are not allowed to modify this test");
        }

        Question question = getQuestion(request, test);

        questionRepository.save(question);

        return mapToQuestionResponse(question);
    }

    private static Question getQuestion(QuestionCreateRequest request, Test test) {
        Question question = new Question();
        question.setText(request.getText());
        question.setContent(request.getContent());
        question.setValue(request.getValue());
        question.setType(request.getType());

        // test_score += question_score

        Double score = test.getScore() + request.getValue();
        test.setScore(score);

        question.setTest(test);

        List<Answer> answers = new ArrayList<>();
        if (request.getAnswers() != null) {
            for (AnswerCreateRequest answerRequest : request.getAnswers()) {
                Answer answer = new Answer();
                answer.setText(answerRequest.getText());
                answer.setIsCorrect(answerRequest.getIsCorrect());
                answer.setQuestion(question);
                answers.add(answer);
            }
        }
        question.setAnswers(answers);
        return question;
    }

    public static QuestionResponse mapToQuestionResponse(Question question) {
        QuestionResponse response = new QuestionResponse();
        response.setId(question.getId());
        response.setText(question.getText());
        response.setContent(question.getContent());
        response.setValue(question.getValue());
        response.setType(question.getType());

        List<QuestionResponse.AnswerResponse> answerResponses = question.getAnswers().stream()
                .map(answer -> {
                    QuestionResponse.AnswerResponse answerResponse = new QuestionResponse.AnswerResponse();
                    answerResponse.setId(answer.getId());
                    answerResponse.setText(answer.getText());
                    answerResponse.setIsCorrect(answer.getIsCorrect());
                    return answerResponse;
                })
                .toList();

        response.setAnswers(answerResponses);

        return response;
    }

    private void authenticateTeacherByCourse(Long courseId, String jwt){
        Course courseById = courseService.getCourseById(courseId);
        Teacher teacherByJWT = teacherService.getTeacherByJWT(jwt);
        if(!courseById.getTeacher().equals(teacherByJWT)){
            throw new AccessDeniedException("No permission to create a test");
        }
    }




    public List<TestResponse> getTestsByCourseId(Long courseId) {
        Course courseById = courseService.getCourseById(courseId);
        List<Test> testsByCourse = testRepository.getTestsByCourse(courseById);
        return testsByCourse.stream().map((TestMapper::toDto)).toList();
    }

    public void deleteById(Long id) {
        testRepository.deleteById(id);
    }
}



