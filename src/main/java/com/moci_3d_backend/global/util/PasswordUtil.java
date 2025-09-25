package com.moci_3d_backend.global.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

// === 비밀번호 암호화/검증 유틸리티 ===
@Component
public class PasswordUtil {
    
    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    
    // === 비밀번호 암호화 ===
    public static String encode(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }
    
    // === 비밀번호 검증 ===
    public static boolean matches(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
