package com.example.quiztourbackend.service;

import com.example.quiztourbackend.entity.Quiz;
import com.example.quiztourbackend.repository.QuizRepository;
import com.example.quiztourbackend.dto.QuizDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class QuizService {

    private final QuizRepository quizRepository;

    @Autowired
    public QuizService(QuizRepository quizRepository) {
        this.quizRepository = quizRepository;
    }

    // Create a new quiz
    public Quiz createQuiz(QuizDTO quizDTO) {
        Quiz quiz = new Quiz();
        quiz.setName(quizDTO.getName());
        quiz.setCategory(quizDTO.getCategory());
        quiz.setDifficulty(quizDTO.getDifficulty());
        quiz.setStartDate(quizDTO.getStartDate());
        quiz.setEndDate(quizDTO.getEndDate());
        return quizRepository.save(quiz);
    }

    // Get all quizzes
    public List<Quiz> getAllQuizzes() {
        return quizRepository.findAll();
    }

    // Fetch quiz details by id
    public Quiz getQuizById(Long quizId) {
        return quizRepository.findById(quizId)
                .orElseThrow(() -> new RuntimeException("Quiz not found"));
    }

    // Fetch quizzes by category
    public List<Quiz> getQuizzesByCategory(String category) {
        return quizRepository.findByCategory(category);
    }

    // Fetch quizzes by difficulty
    public List<Quiz> getQuizzesByDifficulty(String difficulty) {
        return quizRepository.findByDifficulty(difficulty);
    }

    // Update an existing quiz
    public Quiz updateQuiz(Long quizId, QuizDTO quizDTO) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new RuntimeException("Quiz not found"));
        quiz.setName(quizDTO.getName());
        quiz.setCategory(quizDTO.getCategory());
        quiz.setDifficulty(quizDTO.getDifficulty());
        quiz.setStartDate(quizDTO.getStartDate());
        quiz.setEndDate(quizDTO.getEndDate());
        return quizRepository.save(quiz);
    }

    // Delete a quiz
    public void deleteQuiz(Long quizId) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new RuntimeException("Quiz not found"));
        quizRepository.delete(quiz);
    }

    // Fetch questions dynamically from OpenTDB API and map to application's data structure
    public List<QuizDTO> fetchQuestionsFromAPI() {
        String apiUrl = "https://opentdb.com/api.php?amount=10";
        RestTemplate restTemplate = new RestTemplate();

        // Fetch data from the OpenTDB API
        Map<String, Object> response = restTemplate.getForObject(apiUrl, Map.class);
        if (response == null || response.get("results") == null) {
            throw new RuntimeException("Failed to fetch questions from OpenTDB API");
        }

        @SuppressWarnings("unchecked")
        List<Map<String, Object>> results = (List<Map<String, Object>>) response.get("results");

        // Map OpenTDB questions to QuizDTO
        return results.stream()
                .map(this::mapToQuizDTO)
                .collect(Collectors.toList());
    }

    // Map OpenTDB API response to QuizDTO
    private QuizDTO mapToQuizDTO(Map<String, Object> questionData) {
        QuizDTO quizDTO = new QuizDTO();

        // Set the question text
        quizDTO.setQuestion(questionData.get("question").toString());

        // Set the correct answer
        String correctAnswer = questionData.get("correct_answer").toString();
        quizDTO.setCorrectAnswer(correctAnswer);

        // Combine correct and incorrect answers into options
        @SuppressWarnings("unchecked")
        List<String> incorrectAnswers = (List<String>) questionData.get("incorrect_answers");
        List<String> options = new ArrayList<>(incorrectAnswers);
        options.add(correctAnswer);

        // Shuffle the options for random display
        Collections.shuffle(options);
        quizDTO.setOptions(options);

        // Map additional fields
        quizDTO.setCategory(questionData.get("category").toString());
        quizDTO.setDifficulty(questionData.get("difficulty").toString());

        return quizDTO;
    }
}
