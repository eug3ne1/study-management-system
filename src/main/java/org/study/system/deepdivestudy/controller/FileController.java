package org.study.system.deepdivestudy.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.study.system.deepdivestudy.service.StorageService;

@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
public class FileController {

    private final StorageService storageService;

    @GetMapping("/lectures/{fileName:.+}")
    public ResponseEntity<ByteArrayResource> downloadLecturePdf(@PathVariable String fileName) {
        byte[] data = storageService.loadFile("lectures/" + fileName);
        ByteArrayResource resource = new ByteArrayResource(data);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .contentType(MediaType.APPLICATION_PDF)
                .contentLength(data.length)
                .body(resource);
    }

    @GetMapping("/tasks/{fileName:.+}")
    public ResponseEntity<ByteArrayResource> downloadTaskPdf(@PathVariable String fileName) {
        byte[] data = storageService.loadFile("tasks/" + fileName);
        ByteArrayResource resource = new ByteArrayResource(data);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .contentType(MediaType.APPLICATION_PDF)
                .contentLength(data.length)
                .body(resource);
    }


    @GetMapping("/submissions/{fileName:.+}")
    public ResponseEntity<ByteArrayResource> downloadSubPdf(@PathVariable String fileName) {
        byte[] data = storageService.loadFile("submissions/" + fileName);
        ByteArrayResource resource = new ByteArrayResource(data);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .contentType(MediaType.APPLICATION_PDF)
                .contentLength(data.length)
                .body(resource);
    }
}
