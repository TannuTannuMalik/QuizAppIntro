import React, { useState } from 'react';
import API from '../services/api'; // Ensure this is your Axios instance properly configured with base URL and headers.

const CreateQuiz = ({ onQuizCreated }) => {
  const [quiz, setQuiz] = useState({
    name: '',
    category: '',
    difficulty: '',
    startDate: '',
    endDate: '',
  });

  const [error, setError] = useState(null); // For error messages

  // Handle input changes dynamically
  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setQuiz((prevQuiz) => ({ ...prevQuiz, [name]: value }));
  };

  // Submit the form
  const handleSubmit = async (e) => {
    e.preventDefault();
    setError(null); // Reset errors before submitting

    try {
      // API call to create the quiz
      const response = await API.post('/quizzes/create', quiz);

      alert('Quiz created successfully!');
      onQuizCreated(response.data); // Pass the newly created quiz to the parent component
      setQuiz({
        name: '',
        category: '',
        difficulty: '',
        startDate: '',
        endDate: '',
      }); // Reset the form
    } catch (error) {
      console.error('Error creating quiz:', error);
      const errorMessage =
        error.response?.data?.message || 'Failed to create quiz. Please try again.';
      setError(errorMessage);
      alert(errorMessage); // Alert the user with the error message
    }
  };

  return (
    <form onSubmit={handleSubmit}>
      <h2>Create Quiz</h2>
      {error && <p className="error">{error}</p>}
      <label>
        Name:
        <input
          type="text"
          name="name"
          value={quiz.name}
          onChange={handleInputChange}
          required
        />
      </label>
      <label>
        Category:
        <input
          type="text"
          name="category"
          value={quiz.category}
          onChange={handleInputChange}
          required
        />
      </label>
      <label>
        Difficulty:
        <select
          name="difficulty"
          value={quiz.difficulty}
          onChange={handleInputChange}
          required
        >
          <option value="">Select Difficulty</option>
          <option value="easy">Easy</option>
          <option value="medium">Medium</option>
          <option value="hard">Hard</option>
        </select>
      </label>
      <label>
        Start Date:
        <input
          type="datetime-local"
          name="startDate"
          value={quiz.startDate}
          onChange={handleInputChange}
          required
        />
      </label>
      <label>
        End Date:
        <input
          type="datetime-local"
          name="endDate"
          value={quiz.endDate}
          onChange={handleInputChange}
          required
        />
      </label>
      <button type="submit">Create Quiz</button>
    </form>
  );
};

export default CreateQuiz;
