package com.assignment.sinyoung;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class SinyoungApplication {

    public static void main(String[] args) {
        SpringApplication.run(SinyoungApplication.class, args);
    }

}
