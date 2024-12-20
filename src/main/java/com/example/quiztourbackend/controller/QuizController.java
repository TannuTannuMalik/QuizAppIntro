package com.example.quiztourbackend.controller;

import com.example.quiztourbackend.dto.QuizDTO;
import com.example.quiztourbackend.entity.Quiz;
import com.example.quiztourbackend.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quizzes")
public class QuizController {

    @Autowired
    private QuizService quizService;

    // Create a new quiz
    @PostMapping
    public ResponseEntity<Quiz> createQuiz(@RequestBody QuizDTO quizDTO) {
        Quiz quiz = quizService.createQuiz(quizDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(quiz);
    }

    // Get all quizzes
    @GetMapping
    public ResponseEntity<List<Quiz>> getAllQuizzes() {
        List<Quiz> quizzes = quizService.getAllQuizzes();
        return ResponseEntity.ok(quizzes);
    }

    // Fetch quizzes dynamically from OpenTDB
    @GetMapping("/dynamic")
    public ResponseEntity<?> fetchDynamicQuizzes() {
        try {
            List<QuizDTO> quizzes = quizService.fetchQuestionsFromAPI();
            return ResponseEntity.ok(quizzes);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to fetch quizzes: " + e.getMessage());
        }
    }

    // Update an existing quiz
    @PutMapping("/{id}")
    public ResponseEntity<Quiz> updateQuiz(@PathVariable Long id, @RequestBody QuizDTO quizDTO) {
        Quiz updatedQuiz = quizService.updateQuiz(id, quizDTO);
        return ResponseEntity.ok(updatedQuiz);
    }

    // Delete a quiz by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteQuiz(@PathVariable Long id) {
        quizService.deleteQuiz(id);
        return ResponseEntity.ok("Quiz deleted successfully.");
    }
}
