package com.moci_3d_backend.domain.user.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserWithdrawRequest {

    @NotNull(message = "탈퇴 확인은 필수입니다")
    private Boolean confirmWithdrawal = false;  // 탈퇴 확인
}
