package com.example.umigatari.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.umigatari.model.quiz;
import com.example.umigatari.service.QuizService;

@Controller

public class QuizController {

    @Autowired
    private QuizService quizService;

    @PostMapping("")
    public String randomThreeQuiz(){
        quizService.randomThreeQuiz();
    }

    @PostMapping("")
    public String showOneQuiz(){
        quizService.readQuiz();
    }

    @PostMapping("")
    public String check(){
        return "check";
    }

    @GetMapping("")
    public String createQuizPage(){
        return "adduserquiz";
    }

    @PostMapping("")
    public String createQuiz(){
        quizService.insertQuiz();
    }

    @GetMapping("")
    public String admin(){
        return "admin";
    }

    @GetMapping("")
    public String adminCreatQuizPage(){
        return "addquiz";
    }

    @PostMapping("")
    public String adminCreateQuiz(){
        quizService.insertQuiz();
    }

    @GetMapping("")
    public String checkQuizPage(){
        quizService.checkListQuiz();
    }

    @PostMapping("")
    public String checkQuiz(){
        quizService.updateQuiz();
    }

    @GetMapping("")
    public String quizListPage(){
        quizService.listQuiz();
    }

    @PostMapping("")
    public String deleteQuiz(){
        quizService.deleteQuiz();
    }

    @PostMapping("")
    public String updateQuiz(){
        quizService.updateQuiz();
    }

    
}
