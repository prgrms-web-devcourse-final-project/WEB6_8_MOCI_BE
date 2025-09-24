package com.moci_3d_backend.global.initData;

import com.moci_3d_backend.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;

import org.springframework.transaction.annotation.Transactional;
import com.moci_3d_backend.domain.user.repository.UserRepository;
import java.time.LocalDateTime;

@Configuration
@Profile("dev")
@RequiredArgsConstructor
public class DevInitData {
    @Autowired
    @Lazy
    private DevInitData self;

    private final UserRepository userRepository;

    @Bean
    ApplicationRunner devInitDataApplicationRunner() {
        return args -> {
            self.memberInit();
        };
    }

    @Transactional
    public void memberInit() {
        if (userRepository.count() > 0) {
            return;
        }

        // === 테스트 데이터 10개 생성 ===
        
        // 1. 관리자 계정
        User admin = new User();
        admin.setUserId("01012345678");
        admin.setLoginType("PHONE");
        admin.setPassword("admin123");
        admin.setName("관리자");
        admin.setEmail("admin@example.com");
        admin.setRole(User.UserRole.ADMIN);
        admin.setDigitalLevel(5);
        admin.setCreatedAt(LocalDateTime.now());
        admin.setUpdatedAt(LocalDateTime.now());
        userRepository.save(admin);

        // 2. 일반 사용자 1
        User user1 = new User();
        user1.setUserId("01023456789");
        user1.setLoginType("PHONE");
        user1.setPassword("user123");
        user1.setName("김철수");
        user1.setEmail("kim@example.com");
        user1.setRole(User.UserRole.USER);
        user1.setDigitalLevel(4);
        user1.setCreatedAt(LocalDateTime.now());
        user1.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user1);

        // 3. 일반 사용자 2
        User user2 = new User();
        user2.setUserId("01034567890");
        user2.setLoginType("PHONE");
        user2.setPassword("test123");
        user2.setName("이영희");
        user2.setEmail("lee@example.com");
        user2.setRole(User.UserRole.USER);
        user2.setDigitalLevel(3);
        user2.setCreatedAt(LocalDateTime.now());
        user2.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user2);

        // 4. 멘토 계정
        User mentor = new User();
        mentor.setUserId("01045678901");
        mentor.setLoginType("PHONE");
        mentor.setPassword("mentor123");
        mentor.setName("박멘토");
        mentor.setEmail("mentor@example.com");
        mentor.setRole(User.UserRole.MENTOR);
        mentor.setDigitalLevel(5);
        mentor.setCreatedAt(LocalDateTime.now());
        mentor.setUpdatedAt(LocalDateTime.now());
        userRepository.save(mentor);

        // 5. 일반 사용자 3
        User user3 = new User();
        user3.setUserId("01056789012");
        user3.setLoginType("PHONE");
        user3.setPassword("pass123");
        user3.setName("최민수");
        user3.setEmail("choi@example.com");
        user3.setRole(User.UserRole.USER);
        user3.setDigitalLevel(2);
        user3.setCreatedAt(LocalDateTime.now());
        user3.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user3);

        // 6. 일반 사용자 4
        User user4 = new User();
        user4.setUserId("01067890123");
        user4.setLoginType("PHONE");
        user4.setPassword("qwer123");
        user4.setName("정수진");
        user4.setEmail("jung@example.com");
        user4.setRole(User.UserRole.USER);
        user4.setDigitalLevel(3);
        user4.setCreatedAt(LocalDateTime.now());
        user4.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user4);

        // 7. 일반 사용자 5
        User user5 = new User();
        user5.setUserId("01078901234");
        user5.setLoginType("PHONE");
        user5.setPassword("asdf123");
        user5.setName("한지영");
        user5.setEmail("han@example.com");
        user5.setRole(User.UserRole.USER);
        user5.setDigitalLevel(1);
        user5.setCreatedAt(LocalDateTime.now());
        user5.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user5);

        // 8. 일반 사용자 6
        User user6 = new User();
        user6.setUserId("01089012345");
        user6.setLoginType("PHONE");
        user6.setPassword("zxcv123");
        user6.setName("윤태호");
        user6.setEmail("yoon@example.com");
        user6.setRole(User.UserRole.USER);
        user6.setDigitalLevel(4);
        user6.setCreatedAt(LocalDateTime.now());
        user6.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user6);

        // 9. 일반 사용자 7
        User user7 = new User();
        user7.setUserId("01090123456");
        user7.setLoginType("PHONE");
        user7.setPassword("123456");
        user7.setName("강미래");
        user7.setEmail("kang@example.com");
        user7.setRole(User.UserRole.USER);
        user7.setDigitalLevel(2);
        user7.setCreatedAt(LocalDateTime.now());
        user7.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user7);

        // 10. 일반 사용자 8
        User user8 = new User();
        user8.setUserId("01012345670");
        user8.setLoginType("PHONE");
        user8.setPassword("password");
        user8.setName("송하늘");
        user8.setEmail("song@example.com");
        user8.setRole(User.UserRole.USER);
        user8.setDigitalLevel(0);
        user8.setCreatedAt(LocalDateTime.now());
        user8.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user8);
    }

}