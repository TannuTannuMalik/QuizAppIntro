package com.example.quiztourbackend.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    // Method to send generic emails
    public void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        message.setFrom("luxeliving071@gmail.com");  // Optional: can be same as spring.mail.username

        try {
            javaMailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to send email: " + e.getMessage());
        }
    }

    // Method for sending a password reset email
    public void sendPasswordResetEmail(String email, String resetLink) {
        String subject = "Password Reset Request";
        String text = "Hello,\n\nWe received a request to reset your password. Click the link below to reset your password:\n\n"
                + resetLink + "\n\nIf you did not request a password reset, please ignore this email.";

        sendEmail(email, subject, text);  // Use the general sendEmail method to send the password reset email
    }
}
