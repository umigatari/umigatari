package com.example.umigatari.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller

public class UserController {
    @Autowired
    private UserController userController;

    public String mail(){
        return "createaccountone";
    }

    public String sendMail(){
        return "createaccountone";
    }

    public String account(){
        return "createaccounttwo";
    }

    public String createAccount(){
        quizService.createAccount();
    }

    public String login(){
        return "login";
    }

    public String loginPassword(){
        quizService.readPassword();
    }

    public String passwordmail(){
        return "forgotpassone";
    }

    public String readmail(){
        quizService.readMail();
    }

    public String password(){
        return "forgotpasstwo";
    }

    public String changePassword(){
        quizService.updatePassword();
        //同じか判定
    }

    public String deleteAccount(){
        quizService.deleteAccount();
    }

    public String getStamp(){
        return "stamp";
    }

    public String ranking(){
        quizService.myRanking();
        quizService.rankingAccount();
        //ページを表示
    }
    
}
