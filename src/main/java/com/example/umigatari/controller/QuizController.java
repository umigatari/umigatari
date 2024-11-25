package com.example.umigatari.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.example.umigatari.model.quiz;
import com.example.umigatari.service.QuizService;

@Controller

public class QuizController {

    @Autowired
    private QuizService quizService;

    public String randomThreeQuiz(){
        quizService.randomThreeQuiz();
    }

    public String showOneQuiz(){
        quizService.readQuiz();
    }

    public String check(){
        return "check";
    }

    public String createQuizPage(){
        return "adduserquiz";
    }

    public String createQuiz(){
        quizService.insertQuiz();
    }

    public String admin(){
        return "admin";
    }

    public String adminCreatQuizPage(){
        return "addquiz";
    }

    public String adminCreateQuiz(){
        quizService.insertQuiz();
    }

    public String checkQuizPage(){
        quizService.checkListQuiz();
    }

    public String checkQuiz(){
        quizService.updateQuiz();
    }

    public String quizListPage(){
        quizService.listQuiz();
    }

    public String deleteQuiz(){
        quizService.deleteQuiz();
    }

    public String updateQuiz(){
        quizService.updateQuiz();
    }

    
}
