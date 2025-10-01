package com.moci_3d_backend.domain.user.service;

import com.moci_3d_backend.domain.user.dto.request.UserDigitalLevelRequest;
import com.moci_3d_backend.domain.user.dto.request.UserEmailUpdateRequest;
import com.moci_3d_backend.domain.user.dto.request.UserLoginRequest;
import com.moci_3d_backend.domain.user.dto.request.UserPhoneCheckRequest;
import com.moci_3d_backend.domain.user.dto.request.UserRegisterRequest;
import com.moci_3d_backend.domain.user.dto.response.UserDigitalLevelResponse;
import com.moci_3d_backend.domain.user.dto.response.UserPhoneCheckResponse;
import com.moci_3d_backend.domain.user.entity.User;
import com.moci_3d_backend.domain.user.repository.UserRepository;
import com.moci_3d_backend.global.exception.ServiceException;
import com.moci_3d_backend.global.util.PasswordUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final AuthTokenService authTokenService;
    private final UserRepository userRepository;

    // === 회원가입 ===
    @Transactional  // 트랜잭션 처리
    public User register(UserRegisterRequest request) {
        // User 엔티티 생성
        User user = request.toEntity();

        //userId 중복 체크
        if ("PHONE".equals(request.getLoginType())) {
            if (request.getUserId() != null &&
                    userRepository.existsByUserId(request.getUserId())) {
                throw new ServiceException(409, "이미 사용 중인 전화번호입니다.");
            }
        }

        // 비밀번호 암호화 (일반 회원가입)
        if ("PHONE".equals(request.getLoginType()) && request.getPassword() != null) {
            String encodedPassword = PasswordUtil.encode(request.getPassword());
            user.setPassword(encodedPassword);
        }
        // 저장 및 반환
        return userRepository.save(user);
    }

    // === 사용자 조회 ===
    public User findByUserId(String userId) {
        Optional<User> userOptional = userRepository.findByUserId(userId);

        if (userOptional.isEmpty()) {
            throw new ServiceException(404, "존재하지 않는 사용자입니다.");
        }

        return userOptional.get();
    }

    public Optional<User> findByUserIdOptional(String userId) {
        Optional<User> userOptional = userRepository.findByUserId(userId);

        return userOptional;
    }

    public Optional<User> findByRefreshToken(String refreshToken) {
        return userRepository.findByRefreshToken(refreshToken);
    }

    public User getReferenceById(long id) {
        return userRepository.getReferenceById(id);
    }

    public String genAccessToken(User user) {
        return authTokenService.genAccessToken(user);
    }

    public User auth(UserLoginRequest request) {
        // 1. UserId 검증
        Optional<User> userOptional = userRepository.findByUserId(request.getUserId());
        if (userOptional.isEmpty()) {
            throw new ServiceException(400, "아이디 또는 비밀번호가 틀렸습니다.");
        }
        User user = userOptional.get();

        // 비밀번호 검증 (BCrypt)
        boolean isPasswordMatch = PasswordUtil.matches(request.getPassword(), user.getPassword());
        if (!isPasswordMatch) {
            throw new ServiceException(400, "아이디 또는 비밀번호가 틀렸습니다.");
        }

        return user;
    }

    public Map<String, Object> payload(String accessToken) {
        return authTokenService.payload(accessToken);
    }

    public User save (User user) {
        return userRepository.save(user);
    }

    // === 전화번호 중복확인 ===
    public UserPhoneCheckResponse checkPhoneDuplicate(UserPhoneCheckRequest request) {
        // 전화번호 정규화 (하이픈 제거)
        String normalizedPhone = normalizePhoneNumber(request.getPhoneNumber());
        
        // 전화번호 중복 체크
        boolean isDuplicate = userRepository.existsByUserId(normalizedPhone);
        
        if (isDuplicate) {
            return UserPhoneCheckResponse.unavailable();
        } else {
            return UserPhoneCheckResponse.available();
        }
    }

    // === 전화번호 정규화 ===
    private String normalizePhoneNumber(String phoneNumber) {
        if (phoneNumber == null) {
            return null;
        }
        return phoneNumber.replaceAll("-", "");
    }
    
    // === 디지털 레벨 설정 ===
    @Transactional
    public UserDigitalLevelResponse updateDigitalLevel(User user, UserDigitalLevelRequest request) {
        // 이미 디지털 레벨이 설정된 경우 재설정 불가
        if (user.getDigitalLevel() != null) {
            throw new ServiceException(400, "디지털 레벨은 이미 설정되었습니다. 재설정이 불가능합니다.");
        }
        
        // 디지털 레벨 계산
        Integer digitalLevel = request.calculateDigitalLevel();
        
        // 사용자 정보 업데이트
        user.updateDigitalLevel(digitalLevel);
        user.setUpdatedAt(java.time.LocalDateTime.now());
        
        // 저장
        userRepository.save(user);
        
        return UserDigitalLevelResponse.of(digitalLevel);
    }
    
    // === 이메일 수정/등록 ===
    @Transactional
    public User updateEmail(User user, UserEmailUpdateRequest request) {
        // 이메일 업데이트 (등록 또는 수정)
        user.updateEmail(request.getEmail());
        user.setUpdatedAt(java.time.LocalDateTime.now());
        
        // 저장
        return userRepository.save(user);
    }
}
