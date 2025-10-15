package com.moci_3d_backend.domain.user.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserDigitalLevelRequest {
    
    @NotNull(message = "디지털 레벨 설문조사 응답은 필수입니다.")
    @Size(min = 5, max = 5, message = "디지털 레벨 설문조사는 5문항입니다.")
    private List<Boolean> answers;

    public Integer calculateDigitalLevel() {
        if (answers == null) {
            return 0;
        }
        return (int) answers.stream().filter(Boolean.TRUE::equals).count();
    }
}
