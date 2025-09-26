package com.moci_3d_backend.domain.fileUpload.controller;

import com.moci_3d_backend.domain.fileUpload.dto.FileUploadDto;
import com.moci_3d_backend.domain.fileUpload.service.FileUploadService;
import com.moci_3d_backend.global.rsData.RsData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
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

    @PostMapping(value = "/file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Transactional
    @Operation(summary = "파일 업로드", description = "파일을 업로드 & 파일 정보를 반환합니다. 교육 자료실 작성시 이 API로 먼저 파일을 업로드하세요.")
    public RsData<FileUploadDto> fileUpload(
            @Parameter(
                    description = "업로드할 파일",
                    required = true,
                    content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE)
            )
            @RequestParam("uploadFile") MultipartFile uploadFile
    ) {
        log.info("FileController.fileUpload() - 파일명: {}", uploadFile.getOriginalFilename());
        try {
            // Service에서 반환하는 FileUploadDto를 그대로 클라이언트에게 전달
            FileUploadDto uploadedFile = fileUploadService.uploadFile(uploadFile);
            return RsData.of(201, "파일 업로드가 완료되었습니다.", uploadedFile);

        } catch (Exception e) {
            log.error("파일 업로드 중 오류 발생: {}", e.getMessage(), e);
            return RsData.failOf("파일 업로드 중 오류가 발생했습니다: " + e.getMessage());
        }
    }
}
