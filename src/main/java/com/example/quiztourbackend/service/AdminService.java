package com.example.quiztourbackend.service;

import com.example.quiztourbackend.entity.Quiz;
import com.example.quiztourbackend.entity.User;
import com.example.quiztourbackend.repository.QuizRepository;
import com.example.quiztourbackend.repository.UserRepository;
import com.example.quiztourbackend.dto.QuizDTO;
import com.example.quiztourbackend.dto.UserDTO;
import com.example.quiztourbackend.util.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {

    private final QuizRepository quizRepository;
    private final UserRepository userRepository;
    private final EmailService emailService; // Assuming you have an EmailService to send notifications

    @Autowired
    public AdminService(QuizRepository quizRepository, UserRepository userRepository, EmailService emailService) {
        this.quizRepository = quizRepository;
        this.userRepository = userRepository;
        this.emailService = emailService; // Injecting the EmailService
    }

    public Quiz createQuizTournament(QuizDTO quizDTO) {
        Quiz quiz = new Quiz();
        quiz.setName(quizDTO.getName());
        quiz.setCategory(quizDTO.getCategory());
        quiz.setDifficulty(quizDTO.getDifficulty());
        quiz.setStartDate(quizDTO.getStartDate());
        quiz.setEndDate(quizDTO.getEndDate());
        return quizRepository.save(quiz);
    }

    public Quiz updateQuizTournament(Long quizId, QuizDTO quizDTO) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new RuntimeException("Quiz not found"));
        quiz.setName(quizDTO.getName());
        quiz.setCategory(quizDTO.getCategory());
        quiz.setDifficulty(quizDTO.getDifficulty());
        quiz.setStartDate(quizDTO.getStartDate());
        quiz.setEndDate(quizDTO.getEndDate());
        return quizRepository.save(quiz);
    }
    // Delete a quiz tournament
    public void deleteQuizTournament(Long quizId) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new RuntimeException("Quiz not found"));
        quizRepository.delete(quiz);
    }

    public void notifyUsersAboutNewQuiz(Long quizId) {
        List<User> users = userRepository.findAll();
        users.stream().filter(user -> !user.getUsername().equals("admin"))
                .forEach(user -> {
                    Quiz quiz = quizRepository.findById(quizId)
                            .orElseThrow(() -> new RuntimeException("Quiz not found"));
                    emailService.sendQuizNotificationEmail(user.getEmail(), quiz.getName(), quiz.getId());
                });
    }

    // Get all quizzes
    public List<Quiz> getAllQuizzes() {
        return quizRepository.findAll();
    }

    // Get the number of likes for a quiz
    public int getQuizLikes(Long quizId) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new RuntimeException("Quiz not found"));
        return quiz.getLikes();
    }

    // Increment the number of likes for a quiz
    public void incrementQuizLikes(Long quizId) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new RuntimeException("Quiz not found"));
        quiz.incrementLikes(); // Increment the likes
        quizRepository.save(quiz); // Save the updated quiz with incremented likes
    }

    // Create a new admin user
    public User createAdmin(UserDTO userDTO) {
        User admin = new User();
        admin.setUsername(userDTO.getUsername());
        admin.setFirstName(userDTO.getFirstName());
        admin.setLastName(userDTO.getLastName());
        admin.setEmail(userDTO.getEmail());
        // Additional attributes can be added if required
        return userRepository.save(admin);
    }

    // Optionally, you can add more methods for additional admin functionalities
}
