package com.example.taskremainder.controller;

import com.example.taskremainder.service.EmailService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
public class HealthController {

    private final EmailService emailService;

    public HealthController(EmailService emailService) {
        this.emailService = emailService;
    }

    @GetMapping("/health")
    public String health() {
        return "OK - Server Time (IST): " + LocalDateTime.now();
    }

    @GetMapping("/test-email")
    public String testEmail(@RequestParam(required = false, defaultValue = "taskremainder001@gmail.com") String to) {
        return emailService.sendTestEmail(to);
    }
}