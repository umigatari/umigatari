package com.example.umigatari.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.umigatari.model.quiz;
import com.example.umigatari.repository.QuizRepository;

@Service
public class QuizService {
    @Autowired
    private QuizRepository quizRepository;

    public quiz readQuiz(Long id){
        return quizRepository.readQuiz(id);
    }

    public List<quiz> randomThreeQuiz(){
        return quizRepository.randomThreeQuiz();
    }

    public void insertQuiz(quiz quiz){
        quizRepository.insertQuiz(quiz);
    }

    public void deleteQuiz(Long id){
        quizRepository.deleteQuiz(id);
    }

    public void updateQuiz(quiz quiz){
        quizRepository.updateQuiz(quiz);
    }

    public List<quiz> listQuiz(){
        return quizRepository.listQuiz();
    }

    public List<quiz> checkListQuiz(){
        return quizRepository.checkListQuiz();
    }
    
}
