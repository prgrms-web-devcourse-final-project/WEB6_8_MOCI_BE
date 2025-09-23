package com.moci_3d_backend.domain.archive.archive_request.dto;

import com.moci_3d_backend.domain.archive.archive_request.entity.RequestStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ArchiveRequestStatusUpdateDto {
    @NotNull(message = "상태는 필수입니다.")
    private RequestStatus status;
}
