package org.study.system.deepdivestudy.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.study.system.deepdivestudy.dto.LectureDTO;
import org.study.system.deepdivestudy.model.course.Lecture;
import org.study.system.deepdivestudy.service.LectureService;

import java.util.List;

@RestController
@RequestMapping("/api/lectures")
@RequiredArgsConstructor
public class LectureController {

    private final LectureService lectureService;

    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<Lecture>> getAllLectures(@PathVariable Long courseId) {
        return new ResponseEntity<>(lectureService.getAllLecturesByCourse(courseId), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Lecture> getLecture(@PathVariable Long id) {
        return lectureService.getLectureById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @PostMapping(
            value = "/create",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<Lecture> createLecture(
            @RequestPart("lecture") LectureDTO lectureDTO,
            @RequestPart(value = "file", required = false) MultipartFile[] files ,
            @RequestHeader("Authorization") String jwt
    ) {

        Lecture lecture = lectureService.createLecture(lectureDTO, jwt);
        if (files != null) {
            for (MultipartFile file : files) {
                lectureService.uploadPdf(lecture, file, jwt);
            }
        }
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(lecture);
    }


    @PutMapping("/{id}/update")
    public ResponseEntity<Lecture> updateLecture(
            @PathVariable Long id,
            @RequestPart("lecture") LectureDTO lectureDTO,
            @RequestPart(value = "file", required = false) MultipartFile[] files,
            @RequestHeader("Authorization") String jwt
    ) {
        Lecture lecture = lectureService.updateLecture(id, lectureDTO, jwt);
        if (files != null) {

            for (MultipartFile file : files) {
                lectureService.uploadPdf(lecture, file, jwt);
            }
        }
        return ResponseEntity.ok(lecture);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLecture(@PathVariable Long id,
                                              @RequestHeader("Authorization") String jwt) {
        lectureService.deleteLecture(id, jwt);
        return ResponseEntity.noContent().build();
    }



//    @PostMapping("/{lectureId}/upload-pdf")
//    public ResponseEntity<String> uploadPdf(
//            @PathVariable Long lectureId,
//            @RequestParam("file") MultipartFile file,
//            @RequestHeader("Authorization") String jwt) {
//        String pdfUrl = lectureService.uploadPdf(lectureId, file,jwt);
//        return ResponseEntity.ok(pdfUrl);
//    }

    @DeleteMapping("/{lectureId}/file/{fileId}")
    public ResponseEntity<String> deleteFile(@PathVariable Long lectureId, @PathVariable Long fileId){
        lectureService.deleteFile(lectureId, fileId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
