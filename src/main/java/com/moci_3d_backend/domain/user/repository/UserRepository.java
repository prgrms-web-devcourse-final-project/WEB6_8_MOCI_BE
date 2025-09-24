package com.moci_3d_backend.domain.user.repository;

import com.moci_3d_backend.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    // === 로그인 ===
    Optional<User> findByUserId(Integer userId);  // userId(전화번호)로 사용자 찾기
    Optional<User> findBySocialId(String socialId);
    Optional<User> findByUserIdAndPassword(Integer userId, String password);  // userId(전화번호) + 비밀번호로 로그인
    
    // === 중복 체크 ===
    boolean existsByUserId(Integer userId);  // userId(전화번호) 중복 체크
    boolean existsBySocialId(String socialId);
}
