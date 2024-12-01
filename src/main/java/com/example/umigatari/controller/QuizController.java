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

    /*セッションは二つあって解いたtypeを保管するのsolvedQuizzesと、正解したtypeを保管するのcorrectがある。
     * スタンプに表示するのはcorrect
     * クイズを２かい解けなくさせるのはsolvedQuizzes
     */

    //typeごとのクイズを表示する
    @SuppressWarnings("unchecked")
    @GetMapping("quiz/{type}")
    public String randomThreeQuiz(@PathVariable("type") int type, Model model, HttpSession session) {
        Set<Integer> solvedQuizzes = (Set<Integer>) session.getAttribute("solvedQuizzes");
        //リスト作成
        if (solvedQuizzes == null) {
            solvedQuizzes = new LinkedHashSet<>();
        }
        //すでに解いているか判断。解いていれば、問題は表示されない。
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

    //ひとつ選ばれたクイズを表示する
    @PostMapping("quiz/{id}")
    public String showOneQuiz(@PathVariable("id") Long id, Model model) {
        quiz quiz = quizService.selectOneById(id);
        model.addAttribute("quiz", quiz);
        return "quiz/quiz";
    }

    //クイズの回答を表示する
    @SuppressWarnings("unchecked")
    @PostMapping("quiz/answer")
    public String check(@RequestParam String choice,@RequestParam int type, Model model,HttpSession session) {
        //正解なら正解を表示して、せっしょんにあってるよってるのを付与する。
        if ("correct".equals(choice)) {
            model.addAttribute("message", "正解");
            Set<Integer> correct = (Set<Integer>) session.getAttribute("carrect");
            if (correct == null) {
                correct = new LinkedHashSet<>();
            }
            correct.add(type);
            session.setAttribute("correct", correct);
        //不正解なら不正解と表示
        } else {
            model.addAttribute("message", "不正解");
        }
        return "quiz/answer";
    }

    //クイズ作成のページを表示
    @GetMapping("quiz/createpage")
    public String createQuizPage() {
        return "quiz/adduserquiz";
    }


    @SuppressWarnings("unchecked")
    @PostMapping("quiz/create")
    public String createQuiz(@ModelAttribute quiz quiz, Model model, HttpSession session) {
        //セッションに含まれるtypeを確認しそのtypeの問題が作成できるよにする
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


    //管理者ページ


    //adminページを表示　ok
    @GetMapping("admin")
    public String admin(Model model){
        int notice = quizService.getNotice();
        if(notice>=99){
            notice=99;
        }
        model.addAttribute("notice",notice);
        return "admin/admin";
    }

    //問題作成ページを表示　ok
    @GetMapping("admin/create")
    public String adminCreatQuizPage(HttpSession session,Model model){
        Object sessionId = session.getAttribute("quizid");
        //更新ボタンが押された時の処理
        if(sessionId != null){
            Long sessionIdLong = (Long) sessionId;
            session.removeAttribute("quizid");
            quiz quiz =quizService.selectOneById(sessionIdLong);
            model.addAttribute("quiz",quiz);
            
        }
        return "admin/createquiz";
    }

    //問題作成　ok
    @PostMapping("admin/create")
    public String adminCreateQuiz(@ModelAttribute quiz quiz,Model model){
        //問題作成なのか更新なのかチェック
        if(quiz.getId()==null){
            quizService.insertQuiz(quiz);
        model.addAttribute("create", "問題を作成しました!");  
        //問題チェック画面からの遷移の場合 
        }else{
        quizService.updateQuiz(quiz);
        if(quiz.isConfirmation()==true){
            model.addAttribute("posting", "編集したのでこの問題を投稿しますか？");
            model.addAttribute("id", quiz.getId());
        //一覧画面からの遷移の場合    
        }else{
        model.addAttribute("create", "更新しました!");}}
        return "admin/createquiz";
    }

    //問題チェック画面表示 ok
    @GetMapping("admin/check")
    public String checkQuizPage(Model model){
        //チェックが必要なクイズを表示。なければな”問題はありません”と表示
        try {
            List<quiz> quizzes = quizService.checkListQuiz();
            model.addAttribute("quiz", quizzes);
            return "admincheck";
        } catch (NotFoundException e) {
            model.addAttribute("errorMessage", "問題はありません");
            return "admin/check";
        }
    }

    /*@PostMapping("admin/check")
    public String checkQuiz(){
        quizService.updateQuiz();
    }*/

    //クイズ一覧 ok
    @GetMapping("admin/quizlist")
    public String quizListPage(Model model,@RequestParam(value = "p_cocid", required = false) Integer pCocid,
    @RequestParam(value = "dord", required = false) String dord){
        System.out.print(dord);
        if (pCocid != null && dord != null) {
            // 両方のパラメータがある場合の処理
            List<quiz> quiz= quizService.readOrderTypeQuiz(dord,pCocid);
            model.addAttribute("quiz",quiz);
            return "admin/quizlist";
        } else if (pCocid != null) {
            // p_cocidのみがある場合の処理
            List<quiz> quiz= quizService.selectByType(pCocid);
            model.addAttribute("quiz",quiz);
            return "admin/quizlist";
        } else if (dord != null) {
            // dordのみがある場合の処理
            List<quiz> quiz= quizService.selectByOrder(dord);
            model.addAttribute("quiz",quiz);
            return "admin/quizlist";
        } else {
            // 両方ともない場合の処理
            List<quiz> quiz= quizService.listQuiz();
            model.addAttribute("quiz",quiz);
            return "admin/quizlist";
        }
    }

    //クイズ削除 問題チェック画面からok
    @PostMapping("admin/delete")
    public String deleteQuiz(@RequestParam Long id){
        quizService.deleteQuiz(id);
        return "redirect:/admin/check";
    }

    //クイズ削除 一覧からok
    @PostMapping("admin/delete2")
    public String deleteQuiz2(@RequestParam Long id){
        quizService.deleteQuiz(id);
        return "redirect:/admin/quizlist";
    }

    //チェック済みにする　ok
    @PostMapping("admin/update")
    public String updateQuiz(@RequestParam Long id){
        quizService.updateConfirmation(id);
        return "redirect:/admin/check";
        //quizService.updateQuiz(quiz);
    }

    //クイズ更新ページに遷移
    @PostMapping("/editing")
    public String editingCountry(@RequestParam("id") long id, HttpSession session) {
        session.setAttribute("quizid", id);
        return "redirect:/admin/create";
    }

}
