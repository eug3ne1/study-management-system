package org.study.system.deepdivestudy.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.study.system.deepdivestudy.dto.TaskGradeDTO;
import org.study.system.deepdivestudy.dto.TestGradeDTO;
import org.study.system.deepdivestudy.entity.course.Course;
import org.study.system.deepdivestudy.entity.task.Task;
import org.study.system.deepdivestudy.entity.task.TaskGrade;
import org.study.system.deepdivestudy.entity.testing.TestGrade;
import org.study.system.deepdivestudy.entity.testing.Test;
import org.study.system.deepdivestudy.entity.users.Student;
import org.study.system.deepdivestudy.repository.TaskGradeRepository;
import org.study.system.deepdivestudy.repository.TestGradeRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GradeService {

    private final TestGradeRepository testGradeRepository;
    private final TaskGradeRepository taskGradeRepository;
    private final CourseService courseService;

//    public List<CourseGradeResponse> getGradesByStudentCourse(Student student, Long courseId) {
//        List<TestGrade> testGrades = gradeRepository.findByStudentUserIdAndCourseId(student.getId(),courseId);
//        Course course = courseService.getCourseById(courseId);
//        if(!course.getStudents().contains(student)){
//            throw new AccessDeniedException("No such student in this course");
//        }
//
//        return testGrades.stream().map(grade -> {
//            Test test = grade.getTest();
//
//            return new CourseGradeResponse(
//                    course.getName(),
//                    test.getName(),
//                    grade.getScore(),
//                    test.getQuestions().stream().mapToDouble(q -> q.getValue() != null ? q.getValue() : 0).sum(),
//                    test.getStartTime()
//            );
//        }).collect(Collectors.toList());
//    }

    public List<TestGradeDTO> getAllTestGradesByCourse(Long courseId){
        Course courseById = courseService.getCourseById(courseId);
        List<TestGradeDTO> grades = new ArrayList<>();
        for (Test test : courseById.getTests()) {
            List<TestGrade> gradesByTest = testGradeRepository.findByTest_Id(test.getId());
            for (TestGrade testGrade : gradesByTest) {
                TestGradeDTO gradeDTO = new TestGradeDTO();
                gradeDTO.setGrade(testGrade.getScore());
                gradeDTO.setTest(testGrade.getTest());
                gradeDTO.setStudent(testGrade.getStudent());
                grades.add(gradeDTO);
            }
        }
        return grades;

    }

    public List<TaskGradeDTO> getAllTaskGradesByCourse(Long courseId){
        Course courseById = courseService.getCourseById(courseId);
        List<TaskGradeDTO> grades = new ArrayList<>();
        for (Task task : courseById.getTasks()) {
            List<TaskGrade> gradesByTask = taskGradeRepository.findByTask_Id(task.getId());
            for (TaskGrade taskGrade : gradesByTask) {
                TaskGradeDTO gradeDTO = new TaskGradeDTO();
                gradeDTO.setGrade(taskGrade.getScore());
                gradeDTO.setTask(taskGrade.getTask());
                gradeDTO.setStudent(taskGrade.getStudent());
                grades.add(gradeDTO);
            }
        }
        return grades;
    }

    public void addTaskGrade(Double grade, Task task, Student student){
        List<TaskGrade> byTaskId = taskGradeRepository.findByTask_Id(task.getId());
        for (TaskGrade taskGrade : byTaskId) {
            if(taskGrade.getStudent().getId().equals(student.getId())){
                taskGrade.setScore(grade);
                taskGradeRepository.save(taskGrade);
                return;
            }
        }

        TaskGrade taskGrade = new TaskGrade();
        taskGrade.setScore(grade);
        taskGrade.setTask(task);
        taskGrade.setStudent(student);
        taskGradeRepository.save(taskGrade);
    }

    public List<TestGradeDTO> getStudentTestGrades(Long courseId, Long studentId) {

        List<TestGrade> testGrades = testGradeRepository.findByStudentIdAndCourseId(studentId, courseId);
        List<TestGradeDTO> grades = new ArrayList<>();
        for (TestGrade testGrade : testGrades) {
            TestGradeDTO gradeDTO = new TestGradeDTO();
            gradeDTO.setGrade(testGrade.getScore());
            gradeDTO.setTest(testGrade.getTest());
            gradeDTO.setStudent(testGrade.getStudent());
            grades.add(gradeDTO);
        }
        return grades;
    }

    public List<TaskGradeDTO> getStudentTaskGrades(Long courseId, Long studentId) {

        List<TaskGrade> taskGrades = taskGradeRepository.findByStudentIdAndCourseId(studentId, courseId);
        List<TaskGradeDTO> grades = new ArrayList<>();
        for (TaskGrade taskGrade : taskGrades) {
            TaskGradeDTO gradeDTO = new TaskGradeDTO();
            gradeDTO.setGrade(taskGrade.getScore());
            gradeDTO.setTask(taskGrade.getTask());
            gradeDTO.setStudent(taskGrade.getStudent());
            grades.add(gradeDTO);
        }
        return grades;
    }
}

