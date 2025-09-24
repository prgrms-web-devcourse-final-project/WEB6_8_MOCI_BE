package com.moci_3d_backend.domain.user.repository;

import com.moci_3d_backend.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    // === 로그인 ===
    Optional<User> findByUserId(Integer userId);  // 전화번호로 사용자 찾기
    Optional<User> findBySocialId(String socialId);
    Optional<User> findByUserIdAndPassword(Integer userId, String password);  // 전화번호 + 비밀번호 로그인
    
    // === 중복 체크 ===
    boolean existsByUserId(Integer userId);  // 전화번호 중복 체크
    boolean existsBySocialId(String socialId);
}
