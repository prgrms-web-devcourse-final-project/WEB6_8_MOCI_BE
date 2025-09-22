package com.moci_3d_backend.domain.fileUpload.controller;

import com.moci_3d_backend.domain.fileUpload.service.FileUploadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
@Slf4j
@Tag(name="File Upload")
@CrossOrigin(origins = "http://localhost:3000") // TODO: 추후 프론트엔드 도메인으로 변경
public class FileUploadController {
    private final FileUploadService fileUploadService;

    @PostMapping("/file")
    @Transactional
    @Operation(summary = "파일 업로드", description = "파일 업로드")
    public ResponseEntity<?> fileUpload(
            @RequestParam("uploadFile") MultipartFile uploadFile
    ) {
        log.info("FileController.fileUpload()");
        fileUploadService.uploadFile(uploadFile);
        return ResponseEntity.ok("파일 업로드 완료");
    }
}
