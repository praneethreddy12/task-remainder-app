package com.example.taskremainder.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class EmailService {

    @Value("${brevo.api.key}")
    private String apiKey;

    @Value("${app.base.url:https://task-remainder-app-xm3k.onrender.com}")
    private String baseUrl;

    private void sendEmail(String toEmail, String subject, String body) {
        try {
            // Build JSON safely with proper escaping for all values
            String json = "{" +
                "\"sender\":{\"name\":\"Task Reminder\",\"email\":\"taskremainder001@gmail.com\"}," +
                "\"to\":[{\"email\":\"" + escapeJson(toEmail) + "\"}]," +
                "\"subject\":\"" + escapeJson(subject) + "\"," +
                "\"textContent\":\"" + escapeJson(body) + "\"" +
                "}";

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.brevo.com/v3/smtp/email"))
                    .header("accept", "application/json")
                    .header("api-key", apiKey)
                    .header("content-type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Brevo response: " + response.statusCode() + " - " + response.body());
        } catch (Exception e) {
            System.out.println("EMAIL FAILED: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private String escapeJson(String value) {
        if (value == null) return "";
        return value.replace("\\", "\\\\")
                    .replace("\"", "\\\"")
                    .replace("\n", "\\n")
                    .replace("\r", "\\r")
                    .replace("\t", "\\t");
    }

    public void sendReminderEmail(String toEmail, String taskTitle) {
        sendEmail(toEmail, "Task Reminder",
                "Reminder: Your task '" + taskTitle + "' is due soon.");
    }

    public void sendVerificationEmail(String toEmail, String token) {
        String link = baseUrl + "/verify?token=" + token;
        sendEmail(toEmail, "Verify Your Account",
                "Click here to verify your account: " + link);
        System.out.println("Verification email sent to: " + toEmail);
    }

    public void sendOtpEmail(String toEmail, String otp) {
        sendEmail(toEmail, "Your Password Reset OTP",
                "Your OTP for password reset is: " + otp + "\n\nThis OTP is valid for 10 minutes.");
    }
}