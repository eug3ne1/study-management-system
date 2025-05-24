package org.study.system.deepdivestudy.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.study.system.deepdivestudy.dto.LectureDTO;
import org.study.system.deepdivestudy.exceptions.CourseNotFoundException;
import org.study.system.deepdivestudy.entity.course.Course;
import org.study.system.deepdivestudy.entity.course.FilePath;
import org.study.system.deepdivestudy.entity.course.Lecture;
import org.study.system.deepdivestudy.entity.users.Teacher;
import org.study.system.deepdivestudy.repository.CourseRepository;
import org.study.system.deepdivestudy.repository.LectureRepository;
import org.study.system.deepdivestudy.service.LectureService;
import org.study.system.deepdivestudy.service.StorageService;
import org.study.system.deepdivestudy.service.TeacherService;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class LectureServiceImpl implements LectureService {

    private LectureRepository lectureRepository;
    private CourseRepository courseRepository;
    private StorageService storageService;
    private TeacherService teacherService;


    public Lecture createLecture(LectureDTO lectureDTO, String jwt) {
        Course course = courseRepository.findById(lectureDTO.getCourseId())
                .orElseThrow(() -> new CourseNotFoundException("Course not found"));

        Teacher teacher = teacherService.getTeacherByJWT(jwt);
        if (!course.getTeacher().getId().equals(teacher.getId())) {
            throw new AccessDeniedException("No permissions to create lectures for this course");
        }

        Lecture lecture = new Lecture();
        lecture.setTitle(lectureDTO.getTitle());
        lecture.setContent(lectureDTO.getContent());
        lecture.setCourse(course);

        return lectureRepository.save(lecture);
    }

    public List<Lecture> getAllLecturesByCourse(Long courseId) {
        return lectureRepository.findByCourse_Id(courseId);
    }


    public Optional<Lecture> getLectureById(Long id) {
        return lectureRepository.findById(id);
    }

    public Lecture updateLecture(Long lectureId, LectureDTO lectureDTO, String jwt) {
        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new RuntimeException("Lecture not found"));

        Teacher teacher = teacherService.getTeacherByJWT(jwt);
        if (!lecture.getCourse().getTeacher().getId().equals(teacher.getId())) {
            throw new AccessDeniedException("You do not have permission to modify this lecture");
        }

        lecture.setTitle(lectureDTO.getTitle());
        lecture.setContent(lectureDTO.getContent());

        return lectureRepository.save(lecture);
    }

    public void deleteLecture(Long lectureId, String jwt) {
        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new RuntimeException("Lecture not found"));
        Teacher teacher = teacherService.getTeacherByJWT(jwt);

        if (!lecture.getCourse().getTeacher().getId().equals(teacher.getId())) {
            throw new AccessDeniedException("No permissions to delete lectures for this course");
        }

        lectureRepository.delete(lecture);
    }


    public void uploadPdf(Lecture lecture, MultipartFile file, String jwt) {

        Teacher teacher = teacherService.getTeacherByJWT(jwt);
        if (!lecture.getCourse().getTeacher().getId().equals(teacher.getId())) {
            throw new AccessDeniedException("You do not have permission to upload PDF for this lecture");
        }

        String pdfUrl = storageService.saveLecturePdf(lecture.getId(), file);
        FilePath filePath = new FilePath();
        filePath.setPath(pdfUrl);

        lecture.getFilesUrl().add(filePath);
        lectureRepository.save(lecture);

    }



    public void deleteFile(Long lectureId, Long fileId) {
        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new RuntimeException("Lecture not found"));


        FilePath filePath = lecture.getFilesUrl().stream()
                .filter(f -> f.getId().equals(fileId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("File not found in lecture"));
        storageService.deletePhysicalFile(filePath.getPath());
        lecture.getFilesUrl().remove(filePath);
        lectureRepository.save(lecture);
    }
}
