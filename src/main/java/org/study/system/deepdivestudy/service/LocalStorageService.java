package org.study.system.deepdivestudy.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class LocalStorageService implements StorageService {

    private static final String UPLOAD_DIR_LECTURES = "uploads/lectures/";
    private static final String UPLOAD_DIR_TASKS = "uploads/tasks/";
    private static final String UPLOAD_SUBS_TASKS = "uploads/submissions/";

    @Override
    public String saveLecturePdf(Long lectureId, MultipartFile file) {

        try {
            String fileName = "lecture_" + lectureId + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(UPLOAD_DIR_LECTURES, fileName);
            Files.createDirectories(filePath.getParent());
            Files.write(filePath, file.getBytes());

            return "/files/lectures/" + fileName;
        } catch (IOException e) {
            throw new RuntimeException("Failed to save file", e);
        }
    }

    @Override
    public String saveTaskPdf(Long taskId, MultipartFile file) {
        try {
            String fileName = "task_" + taskId + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(UPLOAD_DIR_TASKS, fileName);
            Files.createDirectories(filePath.getParent());
            Files.write(filePath, file.getBytes());

            return "/files/tasks/" + fileName;
        } catch (IOException e) {
            throw new RuntimeException("Failed to save file", e);
        }
    }

    @Override
    public String saveTaskSubmission(Long subId, MultipartFile file) {
        try {
            String fileName = "submission_" + subId + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(UPLOAD_SUBS_TASKS, fileName);
            Files.createDirectories(filePath.getParent());
            Files.write(filePath, file.getBytes());

            return "/files/submissions/" + fileName;
        } catch (IOException e) {
            throw new RuntimeException("Failed to save file", e);
        }
    }

    @Override
    public byte[] loadFile(String filePath) {
        try {
            Path fullPath = Paths.get("uploads", filePath);
            return Files.readAllBytes(fullPath);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load file", e);
        }
    }

    @Override
    public void deletePhysicalFile(String storedPath) {
        try {
            // storedPath з БД має вигляд "/files/lectures/lecture_7_notes.pdf"
            String relative = storedPath.replaceFirst("^/files/", ""); // → "lectures/lecture_7_notes.pdf"
            Path fullPath   = Paths.get("uploads", relative);          // → uploads/lectures/…
            Files.deleteIfExists(fullPath);
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete file", e);
        }
    }



}


