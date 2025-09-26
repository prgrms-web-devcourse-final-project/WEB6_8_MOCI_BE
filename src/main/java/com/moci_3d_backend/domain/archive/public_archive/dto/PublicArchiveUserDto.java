package com.moci_3d_backend.domain.archive.public_archive.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PublicArchiveUserDto {
    private final Long id;
    private final String name;
}
