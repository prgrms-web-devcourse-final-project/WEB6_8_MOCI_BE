package com.moci_3d_backend.domain.archive.public_archive.service;

import com.moci_3d_backend.domain.archive.public_archive.dto.PublicArchiveCreateRequest;
import com.moci_3d_backend.domain.archive.public_archive.dto.PublicArchiveListResponse;
import com.moci_3d_backend.domain.archive.public_archive.dto.PublicArchiveResponse;
import com.moci_3d_backend.domain.archive.public_archive.dto.PublicArchiveUpdateRequest;
import com.moci_3d_backend.domain.archive.public_archive.entity.ArchiveCategory;
import com.moci_3d_backend.domain.archive.public_archive.entity.PublicArchive;
import com.moci_3d_backend.domain.archive.public_archive.mapper.PublicArchiveMapper;
import com.moci_3d_backend.domain.archive.public_archive.repository.PublicArchiveRepository;
import com.moci_3d_backend.domain.fileUpload.entity.FileUpload;
import com.moci_3d_backend.domain.fileUpload.repository.FileUploadRepository;
import com.moci_3d_backend.domain.user.entity.User;
import com.moci_3d_backend.domain.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PublicArchiveService {

    private final PublicArchiveRepository publicArchiveRepository;
    private final UserRepository userRepository;
    private final FileUploadRepository fileUploadRepository;

    // 데이터 변환 mapper
    private final PublicArchiveMapper publicArchiveMapper;

    // 교육 자료실 게시물 생성
    @Transactional
    public PublicArchiveResponse createPublicArchive(PublicArchiveCreateRequest request, Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("해당 ID의 사용자를 찾을 수 없습니다." + userId));

        PublicArchive newArchive = new PublicArchive();
        newArchive.setTitle(request.getTitle());
        newArchive.setDescription(request.getDescription());
        newArchive.setCategory(request.getCategory());
        newArchive.setSubCategory(request.getSubCategory());
        newArchive.setUploadedBy(user);

        // 파일 처리 - 3가지 케이스 모두 지원
        processFiles(newArchive, request.getFileIds());

        PublicArchive savedArchive = publicArchiveRepository.save(newArchive);
        return publicArchiveMapper.toResponseDto(savedArchive);
    }

    // 교육 자료실 목록 페이징 조회
    @Transactional(readOnly = true)
    public PublicArchiveListResponse getPublicArchives(Pageable pageable) {
        Page<PublicArchive> entityPage = publicArchiveRepository.findAll(pageable);

        Page<PublicArchiveListResponse.PublicArchiveDto> dtoPage = entityPage.map(publicArchiveMapper::toListDto);

        return new PublicArchiveListResponse(dtoPage);
    }

    // 교육 자료실 상세 조회
    @Transactional(readOnly = true)
    public PublicArchiveResponse getPublicArchive(Long archiveId) {
        PublicArchive archive = publicArchiveRepository.findById(archiveId)
                .orElseThrow(() -> new EntityNotFoundException("해당 ID의 교육 자료실 글을 찾을 수 없습니다: " + archiveId));

        return publicArchiveMapper.toResponseDto(archive);
    }

    // 교육 자료실 게시물 수정
    @Transactional
    public PublicArchiveResponse updatePublicArchive(Long archiveId, PublicArchiveUpdateRequest request) {
        PublicArchive existingArchive = publicArchiveRepository.findById(archiveId)
                .orElseThrow(() -> new EntityNotFoundException("해당 ID의 교육 자료실 글을 찾을 수 없습니다: " + archiveId));
        if (request.getTitle() != null) existingArchive.setTitle(request.getTitle());
        if (request.getDescription() != null) existingArchive.setDescription(request.getDescription());
        if (request.getCategory() != null) existingArchive.setCategory(request.getCategory());
        if (request.getSubCategory() != null) existingArchive.setSubCategory(request.getSubCategory());

        return publicArchiveMapper.toResponseDto(existingArchive);
    }

    // 교육 자료실 게시물 삭제
    @Transactional
    public void deletePublicArchive(Long archiveId) {
        if (!publicArchiveRepository.existsById(archiveId)) {
            throw new EntityNotFoundException("해당 ID의 교육 자료실 글을 찾을 수 없습니다: " + archiveId);
        }
        publicArchiveRepository.deleteById(archiveId);
    }


    // 파일 처리 메서드
    private void processFiles(PublicArchive archive, List<Long> fileIds) {
        // 케이스 1: fileIds가 null인 경우 (파일 없음)
        if (fileIds == null) {
            archive.setFileUploads(Collections.emptyList());
            return;
        }

        // 케이스 2: fileIds가 빈 배열인 경우 (파일 없음)
        if (fileIds.isEmpty()) {
            archive.setFileUploads(Collections.emptyList());
            return;
        }

        // 케이스 3: fileIds에 값이 있는 경우 (파일 있음)
        // 유효한 파일 ID만 필터링 (null과 0 제거)
        List<Long> validFileIds = fileIds.stream()
                .filter(id -> id != null && id > 0)
                .toList();

        // 필터링 후에도 유효한 ID가 없으면 파일 없음으로 처리
        if (validFileIds.isEmpty()) {
            archive.setFileUploads(Collections.emptyList());
            return;
        }

        // 실제 파일들 조회 및 검증
        List<FileUpload> fileUploads = fileUploadRepository.findAllById(validFileIds);

        // 요청한 파일 수와 실제 조회된 파일 수 비교
        if (fileUploads.size() != validFileIds.size()) {
            List<Long> foundIds = fileUploads.stream().map(FileUpload::getId).toList();
            List<Long> notFoundIds = validFileIds.stream()
                    .filter(id -> !foundIds.contains(id))
                    .toList();
            throw new EntityNotFoundException("다음 ID의 파일을 찾을 수 없습니다: " + notFoundIds);
        }

        // 이미 다른 게시글에 연결된 파일 확인
        List<FileUpload> alreadyUsedFiles = fileUploads.stream()
                .filter(file -> file.getPublicArchive() != null)
                .toList();

        if (!alreadyUsedFiles.isEmpty()) {
            List<Long> usedIds = alreadyUsedFiles.stream().map(FileUpload::getId).toList();
            throw new IllegalStateException("이미 사용된 파일들입니다: " + usedIds);
        }

        // 파일들을 현재 게시글에 연결
        archive.setFileUploads(fileUploads);
        fileUploads.forEach(file -> file.setPublicArchive(archive));
    }
}