package com.example.quiztourbackend.dto;

import java.time.LocalDate;
import java.util.List;

public class QuizDTO {

    private String name; // Name of the quiz
    private String category; // Category of the quiz
    private String difficulty; // Difficulty level of the quiz
    private LocalDate startDate; // Start date for the quiz
    private LocalDate endDate; // End date for the quiz

    // Fields for questions and answers (dynamic fetching)
    private String question; // The actual quiz question
    private String correctAnswer; // Correct answer to the question
    private List<String> options; // All options, including correct and incorrect answers

    // Getters and Setters for existing fields
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    // Getters and Setters for dynamic question fields
    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }
}
