package com.moci_3d_backend.domain.fileUpload.dto;

import com.moci_3d_backend.domain.fileUpload.entity.FileUpload;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FileUploadDto {

    private Long id;
    private String file_name;
    private String file_url;
    private String file_type;

    public static FileUploadDto from(FileUpload fileUpload) {
        return new FileUploadDto(
                fileUpload.getId(),
                fileUpload.getFile_name(),
                fileUpload.getFile_url(),
                fileUpload.getFile_type()
        );
    }
}
