package com.example.umigatari.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.umigatari.service.UserService;

@Controller

public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("")
    public String mail(){
        return "createaccountone";
    }

    @PostMapping("")
    public String sendMail(){
        return "createaccountone";
    }

    @GetMapping("")
    public String account(){
        return "createaccounttwo";
    }

    @PostMapping("")
    public String createAccount(){
        userService.createAccount();
    }

    @GetMapping("")
    public String login(){
        return "login";
    }

    @PostMapping("")
    public String loginPassword(){
        userService.readPassword();
    }

    @GetMapping("")
    public String passwordmail(){
        return "forgotpassone";
    }

    @PostMapping("")
    public String readmail(){
        userService.readMail();
    }

    @PostMapping
    public String password(){
        return "forgotpasstwo";
    }

    @PostMapping
    public String changePassword(){
        userService.updatePassword();
        //同じか判定
    }

    @PostMapping
    public String deleteAccount(){
        userService.deleteAccount();
    }

    @GetMapping
    public String getStamp(){
        return "stamp";
    }

    @GetMapping
    public String ranking(){
        userService.myRanking();
        userService.rankingAccount();
        //ページを表示
    }
    
}
