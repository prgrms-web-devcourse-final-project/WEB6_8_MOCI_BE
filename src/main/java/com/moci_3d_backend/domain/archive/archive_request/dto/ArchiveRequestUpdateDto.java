package com.moci_3d_backend.domain.archive.archive_request.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ArchiveRequestUpdateDto {

    @NotBlank(message = "제목은 필수입니다.")
    @Size(max = 255, message = "제목은 최대 255자까지 가능합니다.")
    private String title;

    @NotBlank(message = "설명은 필수입니다.")
    private String description;
}
