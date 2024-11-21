package com.example.quiztourbackend.controller;

import com.example.quiztourbackend.dto.UserDTO;
import com.example.quiztourbackend.entity.Quiz;
import com.example.quiztourbackend.entity.User;
import com.example.quiztourbackend.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/player")
public class PlayerController {

    @Autowired
    private PlayerService playerService;

    // Create a new player user
    @PostMapping("/register")
    public ResponseEntity<User> createPlayer(@RequestBody UserDTO userDTO) {
        User player = playerService.createPlayer(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(player);
    }

    // View ongoing, upcoming, past, and participated quizzes
    @GetMapping("/quizzes")
    public ResponseEntity<Map<String, List<Quiz>>> getQuizzes() {
        Map<String, List<Quiz>> quizzes = playerService.getQuizzes();
        return ResponseEntity.ok(quizzes);
    }

    // Participate in an ongoing quiz
    @PostMapping("/participate/{quizId}")
    public ResponseEntity<String> participateInQuiz(@PathVariable Long quizId, @RequestParam Long playerId) {
        boolean success = playerService.participateInQuiz(quizId, playerId);
        if (success) {
            return ResponseEntity.ok("Participation successful!");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You cannot participate in this quiz.");
        }
    }

    // Answer a question in a quiz
    @PostMapping("/answer")
    public ResponseEntity<String> submitAnswer(@RequestParam Long playerId, @RequestParam Long questionId, @RequestParam String answer) {
        String feedback = playerService.submitAnswer(playerId, questionId, answer);
        return ResponseEntity.ok(feedback);
    }

    // View score after completing a quiz
    @GetMapping("/score/{quizId}")
    public ResponseEntity<Integer> getQuizScore(@PathVariable Long quizId, @RequestParam Long playerId) {
        int score = playerService.getQuizScore(quizId, playerId);
        return ResponseEntity.ok(score);
    }

    // Like a quiz
    @PostMapping("/like/{quizId}")
    public ResponseEntity<String> likeQuiz(@PathVariable Long quizId, @RequestParam Long playerId) {
        playerService.likeQuiz(quizId, playerId);
        return ResponseEntity.ok("Quiz liked successfully!");
    }

    // Unlike a quiz
    @PostMapping("/unlike/{quizId}")
    public ResponseEntity<String> unlikeQuiz(@PathVariable Long quizId, @RequestParam Long playerId) {
        playerService.unlikeQuiz(quizId, playerId);
        return ResponseEntity.ok("Quiz unliked successfully!");
    }
}

