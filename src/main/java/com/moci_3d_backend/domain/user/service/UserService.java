package com.moci_3d_backend.domain.user.service;

import com.moci_3d_backend.domain.user.dto.request.UserLoginRequest;
import com.moci_3d_backend.domain.user.dto.request.UserRegisterRequest;
import com.moci_3d_backend.domain.user.dto.response.UserLoginResponse;
import com.moci_3d_backend.domain.user.entity.User;
import com.moci_3d_backend.domain.user.repository.UserRepository;
import com.moci_3d_backend.global.exception.ServiceException;
import com.moci_3d_backend.global.util.PasswordUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    
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
        
        // 디지털 레벨 계산
        List<Boolean> answers = request.getDigitalLevelAnswers();
        Integer digitalLevel = 0; // 기본값
        if (answers != null && answers.size() == 5) {
            // 5문항 중 true 개수로 레벨 계산 (0~5)
            int Count = answers.stream().mapToInt(answer -> answer ? 1 : 0).sum();
            digitalLevel = Count;
        }
        user.setDigitalLevel(digitalLevel);
           
        // 저장 및 반환
        return userRepository.save(user);
    }
    
    
    // === 로그인 ===
    public UserLoginResponse login(UserLoginRequest request) {

        // 1. UserId 검증
        Optional<User> userOptional = userRepository.findByUserId(request.getUserId());
        if (userOptional.isEmpty()) {
            throw new ServiceException(400, "'아이디 또는 비밀번호가 틀렸습니다.'");
        }
        User user = userOptional.get();
        
        // 비밀번호 검증 (BCrypt)
        boolean isPasswordMatch = PasswordUtil.matches(request.getPassword(), user.getPassword());
        if (!isPasswordMatch) {
            throw new ServiceException(400, "'아이디 또는 비밀번호가 틀렸습니다.'");
        }       
         
        return UserLoginResponse.from(user);
    }
    
    // === 사용자 조회 ===
    public User findByUserId(String userId) {
        Optional<User> userOptional = userRepository.findByUserId(userId);
        
        if (userOptional.isEmpty()) {
            throw new ServiceException(404, "존재하지 않는 사용자입니다.");
        }
        
        return userOptional.get();
    }
}
