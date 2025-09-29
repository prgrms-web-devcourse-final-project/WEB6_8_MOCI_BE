package com.moci_3d_backend.domain.user.repository;

import com.moci_3d_backend.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // === 로그인 ===
    Optional<User> findByUserId(String userId);  // userId(전화번호)로 사용자 찾기

    // === 중복 체크 ===
    boolean existsByUserId(String userId);  // userId(전화번호) 중복 체크
    Optional<User> findByRefreshToken(String refreshToken);
}
