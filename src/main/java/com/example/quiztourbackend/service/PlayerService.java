package com.example.quiztourbackend.service;

import com.example.quiztourbackend.dto.UserDTO;
import com.example.quiztourbackend.entity.Quiz;
import com.example.quiztourbackend.entity.Score;
import com.example.quiztourbackend.entity.User;
import com.example.quiztourbackend.repository.QuizRepository;
import com.example.quiztourbackend.repository.ScoreRepository;
import com.example.quiztourbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PlayerService {

    private final QuizRepository quizRepository;
    private final ScoreRepository scoreRepository;
    private final UserRepository userRepository;

    @Autowired
    public PlayerService(QuizRepository quizRepository, ScoreRepository scoreRepository, UserRepository userRepository) {
        this.quizRepository = quizRepository;
        this.scoreRepository = scoreRepository;
        this.userRepository = userRepository;
    }

    // Method to create a new player
    public User createPlayer(UserDTO userDTO) {
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());  // Assuming password is already encrypted
        return userRepository.save(user);
    }

    // Method to get quizzes (ongoing, upcoming, past, and participated)
    public Map<String, List<Quiz>> getQuizzes() {
        List<Quiz> allQuizzes = quizRepository.findAll();  // Get all quizzes
        // Group quizzes by their status (e.g., ongoing, upcoming, past)
        return allQuizzes.stream().collect(Collectors.groupingBy(quiz -> {
            if (quiz.getStartDate().isAfter(LocalDate.now())) {
                return "Upcoming";
            } else if (quiz.getEndDate().isBefore(LocalDate.now())) {
                return "Past";
            } else {
                return "Ongoing";
            }
        }));
    }

    // Method for a player to participate in a quiz
    public boolean participateInQuiz(Long quizId, Long playerId) {
        Quiz quiz = quizRepository.findById(quizId).orElseThrow(() -> new RuntimeException("Quiz not found"));
        User user = userRepository.findById(playerId).orElseThrow(() -> new RuntimeException("User not found"));

        // Check if the player is already participating in the quiz (check if a score exists for this quiz and user)
        List<Score> existingScores = scoreRepository.findByUserAndQuiz(user, quiz);
        if (!existingScores.isEmpty()) {
            return false;  // Player has already participated
        }

        Score score = new Score(user, quiz);
        scoreRepository.save(score);
        return true;  // Participation successful
    }

    // Method to submit an answer for a quiz question
    public String submitAnswer(Long playerId, Long questionId, String answer) {
        User user = userRepository.findById(playerId).orElseThrow(() -> new RuntimeException("User not found"));
        // Assuming a Question entity and logic to check the correct answer
        // This is where you would check if the player's answer is correct
        return "Answer submitted successfully";  // Return feedback based on the answer
    }

    // Method to get a player's score for a specific quiz
    public int getQuizScore(Long quizId, Long playerId) {
        Quiz quiz = quizRepository.findById(quizId).orElseThrow(() -> new RuntimeException("Quiz not found"));
        User user = userRepository.findById(playerId).orElseThrow(() -> new RuntimeException("User not found"));

        // Find the score for the quiz and user
        List<Score> scores = scoreRepository.findByUserAndQuiz(user, quiz);
        return scores.isEmpty() ? 0 : scores.get(0).getScore();  // Return the score, or 0 if not found
    }

    // Method to like a quiz
    public void likeQuiz(Long quizId, Long playerId) {
        Quiz quiz = quizRepository.findById(quizId).orElseThrow(() -> new RuntimeException("Quiz not found"));
        User user = userRepository.findById(playerId).orElseThrow(() -> new RuntimeException("User not found"));
        // Implement logic to record that the user liked the quiz
    }

    // Method to unlike a quiz
    public void unlikeQuiz(Long quizId, Long playerId) {
        Quiz quiz = quizRepository.findById(quizId).orElseThrow(() -> new RuntimeException("Quiz not found"));
        User user = userRepository.findById(playerId).orElseThrow(() -> new RuntimeException("User not found"));

    }
}
