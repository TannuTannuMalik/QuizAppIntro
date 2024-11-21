package com.example.quiztourbackend.repository;



import com.example.quiztourbackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Find a user by username
    Optional<User> findByUsername(String username);

    // Find a user by email (for login and password reset)
    Optional<User> findByEmail(String email);

    // Check if a user exists by email
    boolean existsByEmail(String email);

    // Check if a user exists by username
    boolean existsByUsername(String username);

    // Find a user by id
    Optional<User> findById(Long id);
}
