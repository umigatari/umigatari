package com.example.umigatari.controller;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.umigatari.model.account;
import com.example.umigatari.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller

public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("createaccount/mail")
    public String mail(){
        return "account/createaccountone";
    }

    @PostMapping("sendmail/mail")
    public String sendMail(){
        //メールを送る処理
        return "account/createaccountone";
    }

    @GetMapping("createaccount/create")
    public String account(@RequestParam("email") String email, Model model){
        model.addAttribute("email", email);
        return "account/createaccounttwo";
    }

    @PostMapping("createaccount/create")
    public String createAccount(@ModelAttribute account account,Model model){
        userService.createAccount(account);
        model.addAttribute("clear","登録しました");
        return "account/createaccounttwo";
    }

    @GetMapping("login")
    public String login(){
        return "account/login";
    }

    @PostMapping("login/password")
    public String loginPassword(@RequestParam String name,@RequestParam String password,HttpSession session, Model model){
        Map<String, Object> result = userService.readPassword(name, password);

        boolean checkpassword = result != null && (boolean) result.get("passwordMatch");
        Long id = result != null ? (Long) result.get("id") : null;

        if(checkpassword&&id!=null){
            session.setAttribute("id", id);
            return "redirect:quiz/stamp";
        }else{
            model.addAttribute("failure", "パスワードもしくはユーザーネームが間違っています");
            return "account/login";
        }      
    }

    @GetMapping("login/forgotpassword")
    public String passwordmail(){
        return "account/forgotpassone";
    }

    @PostMapping("login/forgotpassword")
    public String readmail(){
        //メールを送る処理。上と同様
        return "account/forgotpasswordone";
    }

    @GetMapping("login/password")
    public String password(){
        return "account/forgotpasstwo";
    }

    /*@PostMapping
    public String changePassword(){
        //userService.updatePassword();
        return "nopage";
        //同じか判定
    }*/

    @PostMapping("deleteaccount")
    public String deleteAccount(HttpSession session){
       Object obj = session.getAttribute("id");
       Long id = (Long)obj;
       userService.deleteAccount(id);
       return "nopage";
    }

    @SuppressWarnings("unchecked")
    @GetMapping("stamp")
    public String getStamp(HttpSession session,Model model){
        
        if(session.getAttribute("id")==null){
            return "nopage";
        }
        Object obj = session.getAttribute("id");
       Long id = (Long)obj;
        model.addAttribute("count", userService.getCount(id));
        Set<Integer> correct = (Set<Integer>) session.getAttribute("correct");
        model.addAttribute("correct", correct);
        return "stamp";
    }

    @SuppressWarnings("unchecked")
    @GetMapping("ranking")
    public String ranking(HttpSession session, Model model){
        if(session.getAttribute("id")==null){
            return "nopage";
        }
        int limit = 5;
        Object obj = session.getAttribute("id");
       Long id = (Long)obj;
        Map<String,Object> result=userService.ranking(limit,id);
        List<account> ranking =  (List<account>) result.get("rankingList");
        int myRanking= (int) result.get("myRanking");
        model.addAttribute("ranking", ranking);
        model.addAttribute("myranking", myRanking);
        return "ranking";
    }
    
}
