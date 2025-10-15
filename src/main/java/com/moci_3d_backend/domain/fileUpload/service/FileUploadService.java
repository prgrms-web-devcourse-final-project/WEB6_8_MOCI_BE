package com.moci_3d_backend.domain.fileUpload.service;

import com.moci_3d_backend.domain.fileUpload.dto.FileUploadDto;
import com.moci_3d_backend.domain.fileUpload.entity.FileUpload;
import com.moci_3d_backend.domain.fileUpload.repository.FileUploadRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

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

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    private final S3Client s3Client;

    public FileUploadDto uploadFileS3(MultipartFile file) {
        try {
            String saveName = UUID.randomUUID() + "-" + file.getOriginalFilename();

            // ✅ S3 업로드
            PutObjectRequest putRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(saveName)
                    .contentType(file.getContentType())
                    .build();

            s3Client.putObject(putRequest, RequestBody.fromBytes(file.getBytes()));
            String fileUrl = "https://" + bucketName + ".s3.ap-northeast-2.amazonaws.com/" + saveName;

            log.info("✅ S3 업로드 완료: {}", fileUrl);

            // local에 임시 저장
            File uploadDir = new File(filePath).getAbsoluteFile();
            if (!uploadDir.exists()) {
                boolean created = uploadDir.mkdirs();
                if (created) {
                    log.info("업로드 디렉토리 생성: {}", uploadDir.getAbsolutePath());
                } else if (!uploadDir.exists()) {
                    log.error("업로드 디렉토리 생성 실패: {}", uploadDir.getAbsolutePath());
                    throw new RuntimeException("업로드 디렉토리 생성 실패: " + uploadDir.getAbsolutePath());
                }
            }

            File localFile = new File(uploadDir, saveName);
            file.transferTo(localFile);
            log.info("파일 저장 완료: {}", localFile.getCanonicalPath());

            // DB 저장
            FileUpload fileUpload = new FileUpload(
                    file.getOriginalFilename(),
                    saveName,
                    file.getContentType()
            );

            return FileUploadDto.from(fileUploadRepository.save(fileUpload));
        } catch (IOException e) {
            log.error("❌ 파일 업로드 실패: {}", e.getMessage(), e);
            throw new RuntimeException("S3 업로드 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    public void deleteFile(String fileName) {
        try {
            DeleteObjectRequest deleteRequest = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .build();

            s3Client.deleteObject(deleteRequest);
        } catch (Exception e) {
            throw new RuntimeException("S3 파일 삭제 실패: " + e.getMessage());
        }
    }
}