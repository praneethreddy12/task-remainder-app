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

    public String sendEmail(String toEmail, String subject, String body) {
        if (toEmail == null || toEmail.trim().isEmpty()) {
            String err = "EMAIL SKIPPED: Recipient email is null or empty";
            System.err.println(err);
            return err;
        }

        if (apiKey == null || apiKey.trim().isEmpty()) {
            String err = "EMAIL SKIPPED: BREVO_API_KEY environment variable is not configured!";
            System.err.println(err);
            return err;
        }

        try {
            // Build JSON safely with proper escaping for all values
            String json = "{" +
                "\"sender\":{\"name\":\"Task Reminder\",\"email\":\"taskremainder001@gmail.com\"}," +
                "\"to\":[{\"email\":\"" + escapeJson(toEmail.trim()) + "\"}]," +
                "\"subject\":\"" + escapeJson(subject) + "\"," +
                "\"textContent\":\"" + escapeJson(body) + "\"" +
                "}";

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.brevo.com/v3/smtp/email"))
                    .header("accept", "application/json")
                    .header("api-key", apiKey.trim())
                    .header("content-type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String result = "Brevo response [" + toEmail + "]: status=" + response.statusCode() + " body=" + response.body();
            System.out.println(result);
            return result;
        } catch (Exception e) {
            String err = "EMAIL FAILED for [" + toEmail + "]: " + e.getMessage();
            System.err.println(err);
            e.printStackTrace();
            return err;
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
        sendEmail(toEmail, "Task Reminder: " + taskTitle,
                "Hello,\n\nThis is a reminder that your task '" + taskTitle + "' is due soon.\n\nPlease check your Task Reminder Dashboard.");
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

    public String sendTestEmail(String toEmail) {
        return sendEmail(toEmail, "Test Email from Task Reminder App",
                "If you are receiving this, your Brevo email configuration is working perfectly!");
    }
}