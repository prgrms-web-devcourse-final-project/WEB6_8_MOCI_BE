package com.moci_3d_backend.global.validator;

import com.moci_3d_backend.domain.user.entity.User;
import org.springframework.stereotype.Component;

/**
 * 인증/인가 관련 검증을 담당하는 Validator
 * 
 * Service 계층에서 비즈니스 로직 검증 시 사용
 * - 로그인 여부 확인
 * - 권한(Role) 검증
 * - 작성자 본인 확인
 */
@Component
public class AuthValidator {

    // 로그인 검증
    public void validateLogin(User actor) {
        if (actor == null) {
            throw new IllegalStateException("로그인이 필요합니다.");
        }
    }

    // 관리자 권한 검증
    public void validateAdmin(User actor) {
        validateLogin(actor);
        if (actor.getRole() != User.UserRole.ADMIN) {
            throw new IllegalStateException("관리자 권한이 필요합니다.");
        }
    }

    // 멘토 권한 검증
    public void validateMentor(User actor) {
        validateLogin(actor);
        if (actor.getRole() != User.UserRole.MENTOR) {
            throw new IllegalStateException("멘토 권한이 필요합니다.");
        }
    }

    // 멘토 또는 관리자 권한 검증
    public void validateMentorOrAdmin(User actor) {
        validateLogin(actor);
        if (actor.getRole() != User.UserRole.MENTOR && actor.getRole() != User.UserRole.ADMIN) {
            throw new IllegalStateException("멘토 또는 관리자만 접근 가능합니다.");
        }
    }

    // 작성자 본인 확인
    public void validateOwner(User owner, User actor) {
        if (owner == null || actor == null) {
            throw new IllegalStateException("사용자 정보가 올바르지 않습니다.");
        }
        if (!owner.getId().equals(actor.getId())) {
            throw new IllegalStateException("작성자 본인만 접근 가능합니다.");
        }
    }

    // 작성자 본인 또는 관리자 권한 검증
    public void validateOwnerOrAdmin(User owner, User actor) {
        validateLogin(actor);
        
        // 관리자는 모든 리소스 접근 가능
        if (actor.getRole() == User.UserRole.ADMIN) {
            return;
        }
        
        // 관리자가 아니면 본인 확인
        if (owner == null || !owner.getId().equals(actor.getId())) {
            throw new IllegalStateException("작성자 본인 또는 관리자만 접근 가능합니다.");
        }
    }


    // 특정 역할(Role) 보유 여부 확인 (boolean 반환)
    public boolean hasRole(User actor, User.UserRole role) {
        return actor != null && actor.getRole() == role;
    }

    // 관리자 권한 보유 여부 확인 (boolean 반환)
    public boolean isAdmin(User actor) {
        return hasRole(actor, User.UserRole.ADMIN);
    }

    // 멘토 권한 보유 여부 확인 (boolean 반환)
    public boolean isMentor(User actor) {
        return hasRole(actor, User.UserRole.MENTOR);
    }

    // 작성자 본인 여부 확인 (boolean 반환)
    public boolean isOwner(User owner, User actor) {
        return owner != null && actor != null && owner.getId().equals(actor.getId());
    }
}
