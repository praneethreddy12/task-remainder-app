package com.example.taskremainder;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.TimeZone;

@SpringBootApplication
@EnableScheduling
public class TaskRemainderApplication {

    @PostConstruct
    public void init() {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Kolkata"));
        System.out.println("Default TimeZone set to Asia/Kolkata (IST): " + java.time.LocalDateTime.now());
    }

    public static void main(String[] args) {
        SpringApplication.run(TaskRemainderApplication.class, args);
    }

}
