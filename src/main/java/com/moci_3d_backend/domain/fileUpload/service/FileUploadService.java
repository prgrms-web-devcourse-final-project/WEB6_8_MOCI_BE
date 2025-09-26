package com.moci_3d_backend.domain.fileUpload.service;

import com.moci_3d_backend.domain.fileUpload.dto.FileUploadDto;
import com.moci_3d_backend.domain.fileUpload.entity.FileUpload;
import com.moci_3d_backend.domain.fileUpload.repository.FileUploadRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileUploadService {

    @Value("${file.dir}")
    private String filePath;

    private final FileUploadRepository fileUploadRepository;

    public FileUploadDto uploadFile(MultipartFile file) {
        String saveName = UUID.randomUUID() + "-" + file.getOriginalFilename();

        try {
            // 절대 경로로 자동 변환 + 디렉토리 자동 생성
            File uploadDir = new File(filePath).getAbsoluteFile();
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
                log.info("업로드 디렉토리 생성: {}", uploadDir.getAbsolutePath());
            }

            File localFile = new File(uploadDir, saveName);
            file.transferTo(localFile);
            log.info("파일 저장 완료: {}", localFile.getCanonicalPath());

            FileUpload fileUpload = new FileUpload(
                    file.getOriginalFilename(),
                    saveName,
                    file.getContentType()
            );
            return FileUploadDto.from(fileUploadRepository.save(fileUpload));

        } catch (IllegalStateException | IOException e) {
            log.error("파일 업로드 실패: {}", e.getMessage());
            throw new RuntimeException("파일 업로드 실패: " + e.getMessage(), e);
        }
    }
}