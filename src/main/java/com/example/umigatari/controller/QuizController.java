package com.example.umigatari.controller;


import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.umigatari.NotFoundException;
import com.example.umigatari.model.quiz;
import com.example.umigatari.service.QuizService;

import jakarta.servlet.http.HttpSession;


@Controller

public class QuizController {

    @Autowired
    private QuizService quizService;

    @SuppressWarnings("unchecked")
    @GetMapping("quiz/{type}")
    public String randomThreeQuiz(@PathVariable("type") int type, Model model, HttpSession session) {
        Set<Integer> solvedQuizzes = (Set<Integer>) session.getAttribute("solvedQuizzes");

        if (solvedQuizzes == null) {
            solvedQuizzes = new LinkedHashSet<>();
        }

        if (!solvedQuizzes.contains(type)) {
            solvedQuizzes.add(type);
            session.setAttribute("solvedQuizzes", solvedQuizzes);
            List<quiz> quiz = quizService.randomThreeQuiz(type);
            model.addAttribute("quiz", quiz);
        } else {
            model.addAttribute("soved", "すでに問題を解いています");
        }

        return "quiz/randomquiz";
    }

    @PostMapping("quiz/{id}")
    public String showOneQuiz(@PathVariable("id") Long id, Model model) {
        quiz quiz = quizService.selectOneById(id);
        model.addAttribute("quiz", quiz);
        return "quiz/answer";
    }

    @SuppressWarnings("unchecked")
    @PostMapping("quiz/answer")
    public String check(@RequestParam String choice,@RequestParam int type, Model model,HttpSession session) {
        if ("author".equals(choice)) {
            model.addAttribute("message", "正解");
            Set<Integer> correct = (Set<Integer>) session.getAttribute("carrect");
            if (correct == null) {
                correct = new LinkedHashSet<>();
            }
            correct.add(type);
            session.setAttribute("correct", correct);

        } else {
            model.addAttribute("message", "不正解");
        }
        return "quiz/check";
    }

    @GetMapping("quiz/createpage")
    public String createQuizPage() {
        return "quiz/adduserquiz";
    }

    @SuppressWarnings("unchecked")
    @PostMapping("quiz/create")
    public String createQuiz(@ModelAttribute quiz quiz, Model model, HttpSession session) {
        Set<Integer> solvedQuizzes = (Set<Integer>) session.getAttribute("solvedQuizzes");
        if (solvedQuizzes != null && !solvedQuizzes.isEmpty()) {
            List<Integer> solvedList = new ArrayList<>(solvedQuizzes);
            Integer lastType = solvedList.get(solvedList.size() - 1);
            quiz.setType(lastType);
        }
        quizService.insertQuiz(quiz);
        model.addAttribute("create", "問題を作成しました!");
        return "quiz/adduserquiz";
    }

    @GetMapping("admin")
    public String admin(){
        return "admin/admin";
    }

    @GetMapping("admin/create")
    public String adminCreatQuizPage(){
        return "admin/addquiz";
    }

    @PostMapping("admin/create")
    public String adminCreateQuiz(@ModelAttribute quiz quiz,Model model){
        quizService.insertQuiz(quiz);
        model.addAttribute("create", "問題を作成しました!");
        return "admin/addquiz";
    }

    @GetMapping("admin/check")
    public String checkQuizPage(Model model){
        try {
            List<quiz> quizzes = quizService.checkListQuiz();
            model.addAttribute("quizzes", quizzes);
            return "admin/check";
        } catch (NotFoundException e) {
            model.addAttribute("errorMessage", "問題はありません");
            return "admin/check";
        }
    }

    /*@PostMapping("admin/check")
    public String checkQuiz(){
        quizService.updateQuiz();
    }*/

    @GetMapping("admin/quizlist")
    public String quizListPage(Model model){
       List<quiz> quiz= quizService.listQuiz();
       model.addAttribute("quiz",quiz);
       return "admin/quizlist";
    }

    @PostMapping("admin/delete")
    public void deleteQuiz(@RequestParam Long id){
        quizService.deleteQuiz(id);
    }

    @PostMapping("admin/update")
    public void updateQuiz(@ModelAttribute quiz quiz){
        quizService.updateQuiz(quiz);
    }

    
}
