package org.study.system.deepdivestudy.service;

import org.springframework.web.multipart.MultipartFile;

public interface StorageService {

    String saveLecturePdf(Long lectureId, MultipartFile file);

    String saveTaskPdf(Long taskId, MultipartFile file);

    String saveTaskSubmission(Long subId, MultipartFile file);

    byte[] loadFile(String filePath);

    void deletePhysicalFile(String storedPath);
}
