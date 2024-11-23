package com.example.quiztourbackend.service;

import com.example.quiztourbackend.entity.User;
import com.example.quiztourbackend.repository.UserRepository;
import com.example.quiztourbackend.dto.LoginRequest;
import com.example.quiztourbackend.dto.UserDTO;
import com.example.quiztourbackend.exception.UserNotFoundException;
import com.example.quiztourbackend.util.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jakarta.servlet.http.HttpSession;  // Importing HttpSession
import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final EmailService emailService;

    @Autowired
    public UserService(UserRepository userRepository, EmailService emailService) {
        this.userRepository = userRepository;
        this.emailService = emailService;
    }

    public User registerUser(UserDTO userDTO) {
        logger.info("Attempting to register user with username: {}", userDTO.getUsername());

        // Validate email and username uniqueness
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            logger.error("User with email {} already exists.", userDTO.getEmail());
            throw new RuntimeException("User with this email already exists.");
        }

        if (userRepository.existsByUsername(userDTO.getUsername())) {
            logger.error("User with username {} already exists.", userDTO.getUsername());
            throw new RuntimeException("User with this username already exists.");
        }

        // Create new User from DTO and store the plain text password
        User user = new User(userDTO);
        user.setPassword(userDTO.getPassword()); // Storing plain text password
        user.setRole(userDTO.getRole() != null ? userDTO.getRole() : "player"); // Set role to 'player' by default

        try {
            // Save the user in the repository
            User savedUser = userRepository.save(user);
            logger.info("User registered successfully with username: {}", savedUser.getUsername());
            return savedUser;
        } catch (Exception e) {
            logger.error("Error occurred while registering user: ", e);
            throw new RuntimeException("Error occurred during registration");
        }
    }


    // User Login with Plaintext Password Check
    public User login(LoginRequest loginRequest) {
        String usernameOrEmail = loginRequest.getUsernameOrEmail();
        Optional<User> user;

        logger.info("Attempting login for user: {}", usernameOrEmail);

        // Find user by email or username
        if (isEmail(usernameOrEmail)) {
            user = userRepository.findByEmail(usernameOrEmail);
        } else {
            user = userRepository.findByUsername(usernameOrEmail);
        }

        // If user doesn't exist, throw an exception
        if (user.isEmpty()) {
            logger.error("User with email/username {} not found.", usernameOrEmail);
            throw new UserNotFoundException("User not found.");
        }

        // Check if passwords match (since it's stored in plain text)
        if (!loginRequest.getPassword().equals(user.get().getPassword())) {
            logger.error("Invalid password for user: {}", usernameOrEmail);
            throw new RuntimeException("Invalid password.");
        }

        logger.info("User {} logged in successfully.", usernameOrEmail);
        return user.get();
    }

    // Update User Details
    public User updateUser(Long id, UserDTO userDTO) {
        logger.info("Attempting to update user with ID: {}", id);

        // Find the existing user by ID
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + id + " not found"));

        // Only update the fields provided in the DTO (fields can be optional)
        if (userDTO.getUsername() != null && !userDTO.getUsername().isEmpty()) {
            existingUser.setUsername(userDTO.getUsername());
        }
        if (userDTO.getFirstName() != null && !userDTO.getFirstName().isEmpty()) {
            existingUser.setFirstName(userDTO.getFirstName());
        }
        if (userDTO.getLastName() != null && !userDTO.getLastName().isEmpty()) {
            existingUser.setLastName(userDTO.getLastName());
        }
        if (userDTO.getEmail() != null && !userDTO.getEmail().isEmpty()) {
            existingUser.setEmail(userDTO.getEmail());
        }
        if (userDTO.getPhoneNumber() != null && !userDTO.getPhoneNumber().isEmpty()) {
            existingUser.setPhoneNumber(userDTO.getPhoneNumber());
        }
        if (userDTO.getAddress() != null && !userDTO.getAddress().isEmpty()) {
            existingUser.setAddress(userDTO.getAddress());
        }
        if (userDTO.getProfilePicture() != null && !userDTO.getProfilePicture().isEmpty()) {
            existingUser.setProfilePicture(userDTO.getProfilePicture());
        }

        // If password is updated, update it as well (store in plain text, but ideally hash it)
        if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
            existingUser.setPassword(userDTO.getPassword()); // Update with plain text password
        }

        try {
            // Save updated user
            User updatedUser = userRepository.save(existingUser);
            logger.info("User with ID {} updated successfully.", id);
            return updatedUser;
        } catch (Exception e) {
            logger.error("Error occurred while updating user with ID {}: ", id, e);
            throw new RuntimeException("Error occurred during user update.");
        }
    }

    // Logout User (invalidate session)
    public void logout(HttpSession session) {
        logger.info("Logging out user");

        if (session != null) {
            // Invalidate the session to log out the user
            session.invalidate();
            logger.info("User logged out successfully.");
        } else {
            logger.warn("No session found to invalidate.");
        }
    }

    // Utility to check if input is a valid email address
    private boolean isEmail(String input) {
        String emailPattern = "^[A-Za-z0-9+_.-]+@(.+)$";
        return Pattern.compile(emailPattern).matcher(input).matches();
    }
}
