package com.moci_3d_backend.domain.archive.public_archive.dto;

import com.moci_3d_backend.domain.archive.public_archive.entity.ArchiveCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class PublicArchiveUpdateRequest {
    private final String title;
    private final String description;
    private final ArchiveCategory category;
    private final String subCategory;

    @Schema(
            description = """
                    업데이트할 파일 ID 목록 (선택사항)
                    
                    **동작 방식:**
                    - null 또는 필드 생략: 파일 변경하지 않음 (기존 파일 모두 유지)
                    - 빈 배열 []: 모든 파일 삭제
                    - [1, 2, 3]: 이 ID들만 유지/추가, 나머지는 삭제
                    
                    **예시:**
                    - 기존 파일: [1, 2, 3]
                    - 요청 fileIds: [1, 2, 4]
                    - 결과: 파일 1, 2 유지 + 파일 4 추가, 파일 3 삭제
                    """,
            example = "[1, 2, 4]",
            nullable = true
    )
    private final List<Long> fileIds;
}
