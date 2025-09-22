package com.moci_3d_backend.domain.archive.public_archive.service;

import com.moci_3d_backend.domain.archive.public_archive.dto.PublicArchiveCreateRequest;
import com.moci_3d_backend.domain.archive.public_archive.dto.PublicArchiveListResponse;
import com.moci_3d_backend.domain.archive.public_archive.dto.PublicArchiveResponse;
import com.moci_3d_backend.domain.archive.public_archive.entity.PublicArchive;
import com.moci_3d_backend.domain.archive.public_archive.mapper.PublicArchiveMapper;
import com.moci_3d_backend.domain.archive.public_archive.repository.PublicArchiveRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PublicArchiveService {

    // TODO: UserRepository, FileUploadRepository 주입 필요
    private final PublicArchiveRepository publicArchiveRepository;
//    private final UserRepository userRepository;
//    private final FileUploadRepository fileUploadRepository;

    // 데이터 변환 mapper
    private final PublicArchiveMapper publicArchiveMapper;

    // 교육 자료실 게시물 생성
    @Transactional
    public PublicArchiveResponse createPublicArchive(PublicArchiveCreateRequest request, Long userId) {
        // TODO: user생성 후 주석 해제 필요
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new EntityNotFoundException("해당 ID의 사용자를 찾을 수 없습니다." + userId));

        PublicArchive newArchive = new PublicArchive();
        newArchive.setTitle(request.getTitle());
        newArchive.setDescription(request.getDescription());
        newArchive.setCategory(request.getCategory());
        newArchive.setSubCategory(request.getSubCategory());
//        newArchive.setUploadedBy(user);

        // TODO: file관련 기능 구현 후 주석 해제 필요
//        if (request.getFileId() != null) {
//            FileUpload fileUpload = fileUploadRepository.findById(request.getFileId())
//                    .orElseThrow(() -> new EntityNotFoundException("해당 ID의 파일을 찾을 수 없습니다." + request.getFileId()));
//
//            newArchive.setFileUploads(Collections.singletonList(fileUpload));
//            fileUpload.setPublicArchive(newArchive);
//        }

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
                .orElseThrow(() -> new EntityNotFoundException("해당 ID의 공개 자료실 글을 찾을 수 없습니다." + archiveId));

        return publicArchiveMapper.toResponseDto(archive);
    }

    // 교육 자료실 게시물 수정
    @Transactional
    public PublicArchiveResponse updatePublicArchive(Long archiveId, PublicArchiveCreateRequest request) {
        PublicArchive existingArchive = publicArchiveRepository.findById(archiveId)
                .orElseThrow(() -> new EntityNotFoundException("해당 ID의 공개 자료실 글을 찾을 수 없습니다." + archiveId));
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
            throw new EntityNotFoundException("해당 ID의 공개 자료실 글을 찾을 수 없습니다." + archiveId);
        }
        publicArchiveRepository.deleteById(archiveId);
    }
}