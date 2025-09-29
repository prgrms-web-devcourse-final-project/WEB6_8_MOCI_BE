package com.moci_3d_backend.domain.archive.public_archive.service;

import com.moci_3d_backend.domain.archive.public_archive.dto.*;
import com.moci_3d_backend.domain.archive.public_archive.entity.PublicArchive;
import com.moci_3d_backend.domain.archive.public_archive.mapper.PublicArchiveMapper;
import com.moci_3d_backend.domain.archive.public_archive.repository.PublicArchiveRepository;
import com.moci_3d_backend.domain.fileUpload.entity.FileUpload;
import com.moci_3d_backend.domain.fileUpload.repository.FileUploadRepository;
import com.moci_3d_backend.domain.user.entity.User;
import com.moci_3d_backend.domain.user.repository.UserRepository;
import com.moci_3d_backend.global.util.KoreanTextAnalyzer;
import com.moci_3d_backend.global.validator.AuthValidator;
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
    private final AuthValidator authValidator;

    // 데이터 변환 mapper
    private final PublicArchiveMapper publicArchiveMapper;

    // 교육 자료실 게시물 생성
    @Transactional
    public PublicArchiveResponse createPublicArchive(PublicArchiveCreateRequest request, User actor) {
        authValidator.validateAdmin(actor);

        PublicArchive newArchive = new PublicArchive();
        newArchive.setTitle(request.getTitle());
        newArchive.setDescription(request.getDescription());
        newArchive.setCategory(request.getCategory());
        newArchive.setSubCategory(request.getSubCategory());
        newArchive.setUploadedBy(actor);

        // 파일 처리 - 3가지 케이스 모두 지원
        processFiles(newArchive, request.getFileIds());

        PublicArchive savedArchive = publicArchiveRepository.save(newArchive);
        return publicArchiveMapper.toResponseDto(savedArchive);
    }

    // 교육 자료실 목록 페이징 조회
    @Transactional(readOnly = true)
    public PublicArchiveListResponse getPublicArchives(Pageable pageable) {
        Page<PublicArchive> entityPage = publicArchiveRepository.findAll(pageable);

        Page<PublicArchiveListItemDto> dtoPage = entityPage.map(publicArchiveMapper::toListItemDto);

        return new PublicArchiveListResponse(dtoPage);
    }

    // 교육 자료실 상세 조회
    @Transactional(readOnly = true)
    public PublicArchiveResponse getPublicArchive(Long archiveId) {
        PublicArchive archive = publicArchiveRepository.findById(archiveId)
                .orElseThrow(() -> new EntityNotFoundException("해당 ID의 교육 자료실 글을 찾을 수 없습니다: " + archiveId));

        return publicArchiveMapper.toResponseDto(archive);
    }

    // 교육 자료실 검색 (KOMORAN 형태소 분석 적용)
    @Transactional(readOnly = true)
    public PublicArchiveListResponse searchPublicArchives(String keyword, Pageable pageable) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getPublicArchives(pageable);
        }

        // KOMORAN으로 명사 추출
        List<String> nouns = KoreanTextAnalyzer.extractNouns(keyword);

        // 명사가 없으면 원본 키워드로 검색 (영어, 숫자 등)
        if (nouns.isEmpty()) {
            String[] keywords = keyword.trim().split("\\s+");
            Page<PublicArchive> entityPage = keywords.length == 1 ?
                    publicArchiveRepository.searchByKeyword(keywords[0], pageable) :
                    publicArchiveRepository.searchByKeywords(keywords, pageable);

            Page<PublicArchiveListItemDto> dtoPage = entityPage.map(publicArchiveMapper::toListItemDto);
            return new PublicArchiveListResponse(dtoPage);
        }

        Page<PublicArchive> entityPage;

        if (nouns.size() == 1) {
            // 단일 명사
            entityPage = publicArchiveRepository.searchByKeyword(nouns.get(0), pageable);
        } else {
            // 여러 명사 (QueryDSL)
            entityPage = publicArchiveRepository.searchByKeywords(nouns.toArray(new String[0]), pageable);
        }

        Page<PublicArchiveListItemDto> dtoPage = entityPage.map(publicArchiveMapper::toListItemDto);
        return new PublicArchiveListResponse(dtoPage);
    }

    // 교육 자료실 게시물 수정
    @Transactional
    public PublicArchiveResponse updatePublicArchive(Long archiveId, PublicArchiveUpdateRequest request, User actor) {
        authValidator.validateAdmin(actor);

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
    public void deletePublicArchive(Long archiveId, User actor) {
        authValidator.validateAdmin(actor);

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