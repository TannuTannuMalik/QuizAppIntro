package com.example.quiztourbackend.security;

import com.example.quiztourbackend.entity.User;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenProvider {

    private final String SECRET_KEY = "your-secret-key";  // Replace with a secure key

    // Generate a JWT token
    public String generateToken(User user) {
        Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);  // Set the algorithm (HS256)
        return JWT.create()
                .withSubject(user.getEmail())  // Set the subject to the user's email
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + 86400000))  // 1 day expiration
                .sign(algorithm);  // Sign the token with the SECRET_KEY
    }

    // Validate the token (e.g., during subsequent requests)
    public boolean validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
            JWTVerifier verifier = JWT.require(algorithm).build();  // Set the verifier with the same secret key
            DecodedJWT decodedJWT = verifier.verify(token);  // Verify the token
            return true;
        } catch (JWTVerificationException exception) {
            return false;  // Invalid token
        }
    }
}
