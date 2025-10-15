package com.moci_3d_backend.global.initData;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;

// 이 파일은 삭제해도 괜찮을 것 같습니다.
@Configuration
@Profile("test")
@RequiredArgsConstructor
public class TestInitData {
    @Autowired
    @Lazy
    private TestInitData self;

    @Bean
    ApplicationRunner testInitDataApplicationRunner() {
        return args -> {
//            self.memberInit();
        };
    }

//    @Transactional
//    public void memberInit() {
//        if (memberService.count() > 0) {
//            return;
//        }
//
//        memberService.register("admin","12345678", "admin@gmail.com");
//        memberService.register("user1","12345678", "user1@gmail.com");
//    }

}