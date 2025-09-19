package com.moci_3d_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class Moci3DBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(Moci3DBackendApplication.class, args);
        System.out.println("Hello, Moci 3D Backend!");
    }

}
