package com.moci_3d_backend.domain.fileUpload.controller;

import com.moci_3d_backend.domain.fileUpload.service.FileUploadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
@Slf4j
public class FileUploadController {
    private final FileUploadService fileUploadService;

    @PostMapping("/file")
    public ResponseEntity<?> fileUpload(
            @RequestParam("uploadFile") MultipartFile uploadFile
    ) {
        log.info("FileController.fileUpload()");
        fileUploadService.uploadFile(uploadFile);
        return ResponseEntity.ok("파일 업로드 완료");
    }
}
