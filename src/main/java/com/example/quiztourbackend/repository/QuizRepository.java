package com.example.quiztourbackend.repository;

import com.example.quiztourbackend.entity.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, Long> {

    // Find a quiz by its ID
    Optional<Quiz> findById(Long id);

    // Find all quizzes
    List<Quiz> findAll();

    // Find quizzes by category
    List<Quiz> findByCategory(String category);

    // Find quizzes by difficulty
    List<Quiz> findByDifficulty(String difficulty);

    // Find quizzes that are ongoing (based on current date)
    List<Quiz> findByStartDateBeforeAndEndDateAfter(LocalDate currentDateStart, LocalDate currentDateEnd);

    // Find quizzes that are upcoming (start date is after the current date)
    List<Quiz> findByStartDateAfter(LocalDate currentDate);
}
