package com.example.quiztourbackend.service;

import com.example.quiztourbackend.entity.PasswordResetToken;
import com.example.quiztourbackend.entity.User;
import com.example.quiztourbackend.exception.UserNotFoundException;
import com.example.quiztourbackend.repository.PasswordResetTokenRepository;
import com.example.quiztourbackend.repository.UserRepository;
import com.example.quiztourbackend.util.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PasswordResetService {

    private final UserRepository userRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final EmailService emailService;

    @Autowired
    public PasswordResetService(UserRepository userRepository, PasswordResetTokenRepository tokenRepository, EmailService emailService) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.emailService = emailService;
    }

    // Request password reset by email
    public void requestPasswordReset(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User with this email does not exist"));

        // Generate a password reset token
        String token = UUID.randomUUID().toString();

        // Save the token with expiration time (e.g., 1 hour)
        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setToken(token);
        resetToken.setUser(user);
        resetToken.setExpirationDate(LocalDateTime.now().plusHours(1)); // Token expires in 1 hour
        tokenRepository.save(resetToken);

        // Send an email with the reset link
        String resetLink = "http://localhost:8080/reset-password?token=" + token;
        String emailBody = "To reset your password, click the link below:\n" + resetLink;

        emailService.sendEmail(user.getEmail(), "Password Reset Request", emailBody);
    }

    // Reset the password using the token
    public void resetPassword(String token, String newPassword) {
        PasswordResetToken resetToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Invalid token"));

        // Check if the token is expired
        if (resetToken.getExpirationDate().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Token has expired");
        }

        // Reset the user's password
        User user = resetToken.getUser();
        user.setPassword(newPassword); // Ideally, hash the password before saving it
        userRepository.save(user);

        // Delete the token after successful password reset
        tokenRepository.delete(resetToken);
    }
}
