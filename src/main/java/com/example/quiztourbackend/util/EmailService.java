package com.example.quiztourbackend.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender emailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public EmailService(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    // Send password reset email
    public void sendPasswordResetEmail(String toEmail, String resetLink) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject("Password Reset");
        message.setText("Click here to reset your password: " + resetLink);
        emailSender.send(message);
    }

    // Send notification email for new quiz tournament
    public void sendQuizNotificationEmail(String toEmail, String quizName, Long quizId) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject("New Quiz Tournament Created");
        message.setText("A new quiz tournament '" + quizName + "' (ID: " + quizId + ") has been created. Join now!");
        emailSender.send(message);
    }

    public void sendCustomEmail(String toEmail, String subject, String messageText) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(messageText);
        emailSender.send(message);
    }
}
