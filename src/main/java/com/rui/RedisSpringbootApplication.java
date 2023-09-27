package com.rui;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableWebMvc
public class RedisSpringbootApplication {

    public static void main(String[] args) {
        SpringApplication.run(RedisSpringbootApplication.class, args);
    }

}
