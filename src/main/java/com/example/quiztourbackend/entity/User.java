package com.example.quiztourbackend.entity;

import com.example.quiztourbackend.dto.UserDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters")
    @Column(nullable = false, unique = true)
    private String username;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    @Column(nullable = false)
    private String password;

    @NotBlank(message = "Role is required")
    @Column(nullable = false)
    private String role = "PLAYER";  // Default role is "PLAYER"

    @NotBlank(message = "First name is required")
    @Column(nullable = false)
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Column(nullable = false)
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Column(nullable = false, unique = true)
    private String email;

    private String phoneNumber; // Custom field
    private String address; // Custom field
    private String profilePicture; // Optional field for player/admin profile picture

    @OneToMany(mappedBy = "user")
    private Set<Score> scores = new HashSet<>(); // Track quiz scores

    // Default constructor
    public User() {}

    // Constructor using UserDTO to initialize User fields
    public User(UserDTO userDTO) {
        this.username = userDTO.getUsername();
        this.password = userDTO.getPassword(); // Don't forget to encode password later
        this.role = userDTO.getRole() != null ? userDTO.getRole() : "PLAYER"; // Default to "PLAYER"
        this.firstName = userDTO.getFirstName();
        this.lastName = userDTO.getLastName();
        this.email = userDTO.getEmail();
        this.phoneNumber = userDTO.getPhoneNumber();
        this.address = userDTO.getAddress();
        this.profilePicture = userDTO.getProfilePicture();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public Set<Score> getScores() {
        return scores;
    }

    public void setScores(Set<Score> scores) {
        this.scores = scores;
    }

    // Method to add a Score for the user
    public void addScore(Score score) {
        this.scores.add(score);
    }

    // Method to update user profile from a UserDTO (for profile update)
    public void updateUser(UserDTO userDTO) {
        if (userDTO.getUsername() != null) {
            this.username = userDTO.getUsername();
        }
        if (userDTO.getFirstName() != null) {
            this.firstName = userDTO.getFirstName();
        }
        if (userDTO.getLastName() != null) {
            this.lastName = userDTO.getLastName();
        }
        if (userDTO.getEmail() != null) {
            this.email = userDTO.getEmail();
        }
        if (userDTO.getPhoneNumber() != null) {
            this.phoneNumber = userDTO.getPhoneNumber();
        }
        if (userDTO.getAddress() != null) {
            this.address = userDTO.getAddress();
        }
        if (userDTO.getProfilePicture() != null) {
            this.profilePicture = userDTO.getProfilePicture();
        }

        // If password is updated, make sure to encode the password before saving (if applicable)
        if (userDTO.getPassword() != null) {
            this.password = userDTO.getPassword();  // In real cases, encode password
        }
    }

    // Custom method to check if the user is an Admin
    public boolean isAdmin() {
        return "ADMIN".equals(this.role);
    }

    // Custom method to check if the user is a Player
    public boolean isPlayer() {
        return "PLAYER".equals(this.role);
    }
}
