package com.moci_3d_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
public class Moci3DBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(Moci3DBackendApplication.class, args);
    }

    @EnableJpaAuditing
    @Profile("!test")
    static class JpaAuditingConfig {
    }
}
