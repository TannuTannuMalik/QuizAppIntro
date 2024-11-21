package com.example.quiztourbackend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())  // Disable CSRF protection for API endpoints (stateless)
                .authorizeHttpRequests(authz -> authz
                        // Permit all requests to the registration endpoint and other necessary endpoints
                        //.requestMatchers("/api/users/register", "/api/users/**", "/api/quizzes/**").permitAll()
                        // Require authentication for other endpoints
                        .anyRequest().permitAll()
                );

        return http.build();
    }
}
