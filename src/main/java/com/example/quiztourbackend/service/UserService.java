package com.example.quiztourbackend.service;

import com.example.quiztourbackend.entity.User;
import com.example.quiztourbackend.repository.UserRepository;
import com.example.quiztourbackend.dto.LoginRequest;
import com.example.quiztourbackend.dto.UserDTO;
import com.example.quiztourbackend.exception.UserNotFoundException;
import com.example.quiztourbackend.security.JwtTokenProvider;
import com.example.quiztourbackend.util.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final EmailService emailService;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder; // Use PasswordEncoder interface

    @Autowired
    public UserService(UserRepository userRepository, EmailService emailService, JwtTokenProvider jwtTokenProvider, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.passwordEncoder = passwordEncoder; // Injected PasswordEncoder
    }

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        // Retrieve the user by email or username
        User user = userRepository.findByEmail(usernameOrEmail)
                .or(() -> userRepository.findByUsername(usernameOrEmail))
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username or email: " + usernameOrEmail));

        // Return a UserDetails instance compatible with Spring Security
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername()) // Login username
                .password(user.getPassword()) // Encoded password
                .roles(user.getRole()) // User role(s)
                .build();
    }

    public User registerUser(UserDTO userDTO) {
        if (userRepository.existsByEmail(userDTO.getEmail()) || userRepository.existsByUsername(userDTO.getUsername())) {
            throw new RuntimeException("User already exists!");
        }
        User user = new User(userDTO);
        user.setPassword(passwordEncoder.encode(userDTO.getPassword())); // Encode the password
        return userRepository.save(user);
    }

    public String login(LoginRequest loginRequest) {
        String usernameOrEmail = loginRequest.getUsernameOrEmail();
        Optional<User> user;

        if (isEmail(usernameOrEmail)) {
            user = userRepository.findByEmail(usernameOrEmail);
        } else {
            user = userRepository.findByUsername(usernameOrEmail);
        }

        if (user.isEmpty()) {
            throw new UserNotFoundException("User not found");
        }

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.get().getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        return jwtTokenProvider.generateToken(user.get());
    }

    public User updateUser(Long id, UserDTO userDTO) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + id + " not found"));

        existingUser.setUsername(userDTO.getUsername());
        existingUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        existingUser.setRole(userDTO.getRole());
        existingUser.setFirstName(userDTO.getFirstName());
        existingUser.setLastName(userDTO.getLastName());
        existingUser.setEmail(userDTO.getEmail());
        existingUser.setPhoneNumber(userDTO.getPhoneNumber());
        existingUser.setAddress(userDTO.getAddress());
        existingUser.setProfilePicture(userDTO.getProfilePicture());

        return userRepository.save(existingUser);
    }

    private boolean isEmail(String input) {
        String emailPattern = "^[A-Za-z0-9+_.-]+@(.+)$";
        return Pattern.compile(emailPattern).matcher(input).matches();
    }
}
