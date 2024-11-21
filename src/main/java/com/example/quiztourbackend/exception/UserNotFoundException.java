package com.example.quiztourbackend.exception;

public class UserNotFoundException extends RuntimeException {

    // Constructor that accepts a custom message
    public UserNotFoundException(String message) {
        super(message); // Pass the message to the parent class (RuntimeException)
    }
}
