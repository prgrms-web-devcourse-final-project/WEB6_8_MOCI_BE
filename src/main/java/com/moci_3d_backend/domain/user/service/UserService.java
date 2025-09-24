package com.moci_3d_backend.domain.user.service;

import com.moci_3d_backend.domain.user.dto.request.UserLoginRequest;
import com.moci_3d_backend.domain.user.dto.request.UserRegisterRequest;
import com.moci_3d_backend.domain.user.dto.response.UserLoginResponse;
import com.moci_3d_backend.domain.user.entity.User;
import com.moci_3d_backend.domain.user.repository.UserRepository;
import com.moci_3d_backend.global.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    
    private final UserRepository userRepository;
    // === 회원가입 ===
    @Transactional  // 트랜잭션 처리
    public User register(UserRegisterRequest request) {
        return userRepository.save(request.toEntity());
    }
    
    // === 로그인 ===
    public UserLoginResponse login(UserLoginRequest request) {
        Optional<User> userOptional = userRepository.findByUserIdAndPassword(
            request.getUserId(), 
            request.getPassword()
        );
        
        if (userOptional.isEmpty()) {
            throw new ServiceException(400, "아이디 또는 비밀번호가 틀렸습니다.");
        }
        
        return UserLoginResponse.from(userOptional.get());
    }
    
    public UserLoginResponse socialLogin(String socialId) {
        Optional<User> userOptional = userRepository.findBySocialId(socialId);
        
        if (userOptional.isEmpty()) {
            throw new ServiceException(400, "소셜 로그인에 실패했습니다.");
        }
        
        return UserLoginResponse.from(userOptional.get());
    }
    
    // === 사용자 조회 ===
    public User findByUserId(String userId) {
        Optional<User> userOptional = userRepository.findByUserId(userId);
        
        if (userOptional.isEmpty()) {
            throw new ServiceException(404, "존재하지 않는 사용자입니다.");
        }
        
        return userOptional.get();
    }
    
    public User findBySocialId(String socialId) {
        Optional<User> userOptional = userRepository.findBySocialId(socialId);
        
        if (userOptional.isEmpty()) {
            throw new ServiceException(404, "존재하지 않는 사용자입니다.");
        }
        
        return userOptional.get();
    }
}
