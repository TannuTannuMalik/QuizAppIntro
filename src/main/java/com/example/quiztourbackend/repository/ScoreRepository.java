package com.example.quiztourbackend.repository;


import com.example.quiztourbackend.entity.Quiz;
import com.example.quiztourbackend.entity.Score;
import com.example.quiztourbackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ScoreRepository extends JpaRepository<Score, Long> {

    // Find a score by id
    Optional<Score> findById(Long id);

    // Find all scores for a specific quiz
    List<Score> findByQuizId(Long quizId);

    // Find all scores for a specific user
    List<Score> findByUserId(Long userId);

    // Custom query to find scores by user and quiz
    List<Score> findByUserAndQuiz(User user, Quiz quiz);
    // Find scores for a specific quiz, sorted by score in descending order
    List<Score> findByQuizIdOrderByScoreDesc(Long quizId);

    // Find scores by quiz and user
    Optional<Score> findByQuizIdAndUserId(Long quizId, Long userId);

    // Get the top scores for a specific quiz (e.g., top 5)
    List<Score> findTop5ByQuizIdOrderByScoreDesc(Long quizId);
}
