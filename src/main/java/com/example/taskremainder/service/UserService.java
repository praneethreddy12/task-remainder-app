package com.example.taskremainder.service;

import com.example.taskremainder.Repository.UserRepository;
import com.example.taskremainder.entity.User;
import com.example.taskremainder.model.UserDTO;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final EmailService emailService;
    private final BCryptPasswordEncoder encoder;


    private final Map<String, String> otpStore = new HashMap<>();

    public UserService(UserRepository userRepository, EmailService emailService) {
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.encoder = new BCryptPasswordEncoder();
    }

    //  REGISTER
    public String register(User user) {
        if (userRepository.findByEmail(user.getEmail()) != null) {
            return "Email Already Exists";
        }
        user.setPassword(encoder.encode(user.getPassword()));
        user.setEnabled(false);
        String token = UUID.randomUUID().toString();
        user.setVerificationToken(token);
        userRepository.save(user);
        emailService.sendVerificationEmail(user.getEmail(), token);
        return "SUCCESS";
    }

    //  VERIFY USER
    public boolean verifyUser(String token) {
        User user = userRepository.findByVerificationToken(token);
        if (user != null) {
            user.setEnabled(true);
            user.setVerificationToken(null);
            userRepository.save(user);
            return true;
        }
        return false;
    }

    //  LOGIN
    public UserDTO login(String email, String password) {
        User user = userRepository.findByEmail(email);
        if (user == null) throw new RuntimeException("User not found");
        if (!user.isEnabled()) throw new RuntimeException("Please verify your email first");
        if (!encoder.matches(password, user.getPassword())) throw new RuntimeException("Invalid password");
        return new UserDTO(user.getId(), user.getEmail());
    }

    //  SEND OTP
    public String sendOtp(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) return "EMAIL_NOT_FOUND";

        String otp = String.valueOf(new Random().nextInt(900000) + 100000);
        otpStore.put(email, otp);

        emailService.sendOtpEmail(email, otp);
        return "SUCCESS";
    }

    //  RESET PASSWORD
    public String resetPassword(String email, String otp, String newPassword) {
        String savedOtp = otpStore.get(email);
        if (savedOtp == null) return "OTP not sent. Please request OTP first.";
        if (!savedOtp.equals(otp)) return "Invalid OTP. Please try again.";

        User user = userRepository.findByEmail(email);
        if (user == null) return "User not found.";

        user.setPassword(encoder.encode(newPassword));
        userRepository.save(user);
        otpStore.remove(email);
        return "SUCCESS";
    }
}