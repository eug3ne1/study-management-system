package org.study.system.deepdivestudy.service;

import org.springframework.web.multipart.MultipartFile;
import org.study.system.deepdivestudy.dto.LectureDTO;
import org.study.system.deepdivestudy.entity.course.Lecture;

import java.util.List;
import java.util.Optional;

public interface LectureService {

    Lecture createLecture(LectureDTO lectureDTO, String jwt);

    List<Lecture> getAllLecturesByCourse(Long courseId);

    Optional<Lecture> getLectureById(Long id);

    Lecture updateLecture(Long lectureId, LectureDTO lectureDTO, String jwt);

    void deleteLecture(Long lectureId, String jwt);

    void uploadPdf(Lecture lecture, MultipartFile file, String jwt);

    void deleteFile(Long lectureId, Long fileId);
}

