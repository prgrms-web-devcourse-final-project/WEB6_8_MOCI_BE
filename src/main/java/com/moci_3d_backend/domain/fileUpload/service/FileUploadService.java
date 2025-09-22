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
        String saveName = UUID.randomUUID()+"-"+file.getOriginalFilename();
        FileUpload fileUpload = null;

        try {
            log.info("file path : {}", filePath);
            File localFile = new File(filePath + "/" + saveName);
            file.transferTo(localFile);
            log.info("파일 저장 완료: {}", localFile.getCanonicalPath());
            fileUpload = new FileUpload(file.getOriginalFilename(), saveName, file.getContentType());
        } catch (IllegalStateException | IOException e) {
            throw new RuntimeException(e);
        }
        return FileUploadDto.from(fileUploadRepository.save(fileUpload));
    }

    public FileUpload save(FileUpload fileUpload) {
        return fileUploadRepository.save(fileUpload);
    }

}
