package com.example.quiztourbackend.controller;

import com.example.quiztourbackend.dto.QuizDTO;
import com.example.quiztourbackend.entity.Quiz;
import com.example.quiztourbackend.entity.User;
import com.example.quiztourbackend.service.AdminService;
import com.example.quiztourbackend.util.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private EmailService emailService;

    // Create a new quiz tournament


    // View all quizzes
    @GetMapping("/quizzes")
    public ResponseEntity<List<Quiz>> getAllQuizzes() {
        List<Quiz> quizzes = adminService.getAllQuizzes(); // Assuming a method exists to get all quizzes
        return ResponseEntity.ok(quizzes);
    }

    // Update a quiz tournament
    @PutMapping("/update-quiz/{id}")
    public ResponseEntity<Quiz> updateQuiz(@PathVariable Long id, @RequestBody QuizDTO quizDTO) {
        Quiz updatedQuiz = adminService.updateQuizTournament(id, quizDTO); // Correct method call
        return ResponseEntity.ok(updatedQuiz);
    }

    // Delete a quiz tournament
    @DeleteMapping("/delete-quiz/{id}")
    public ResponseEntity<String> deleteQuiz(@PathVariable Long id) {
        adminService.deleteQuizTournament(id); // Correct method call
        return ResponseEntity.ok("Quiz deleted successfully.");
    }

    // View likes for a quiz tournament (assuming you have a 'likes' field in the Quiz entity)
    @GetMapping("/quiz-likes/{id}")
    public ResponseEntity<Integer> getQuizLikes(@PathVariable Long id) {
        int likes = adminService.getQuizLikes(id); // Assuming a method exists for likes
        return ResponseEntity.ok(likes);
    }
}
