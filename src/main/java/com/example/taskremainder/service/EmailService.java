package com.example.taskremainder.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.mail.javamail.JavaMailSender;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    //  TASK REMINDER
    public void sendReminderEmail(String toEmail, String taskTitle) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Task Reminder");
        message.setText("Reminder: Your task '" + taskTitle + "' is due soon.");
        mailSender.send(message);
    }

    // VERIFICATION EMAIL
    public void sendVerificationEmail(String toEmail, String token) {
        try {
            String link = "http://localhost:8080/verify?token=" + token;
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail);
            message.setSubject("Verify Your Account");
            message.setText("Click here to verify your account: " + link);
            mailSender.send(message);
            System.out.println("Verification email sent to: " + toEmail);
        } catch (Exception e) {
            System.out.println(" EMAIL FAILED: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // OTP EMAIL
    public void sendOtpEmail(String toEmail, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Your Password Reset OTP");
        message.setText("Your OTP for password reset is: " + otp + "\n\nThis OTP is valid for 10 minutes.");
        mailSender.send(message);
    }
}