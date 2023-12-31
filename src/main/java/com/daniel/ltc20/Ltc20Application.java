package com.daniel.ltc20;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class Ltc20Application {

    public static void main(String[] args) {
        SpringApplication.run(Ltc20Application.class, args);
    }
}

