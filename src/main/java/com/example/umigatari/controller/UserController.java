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

    //アカウント作成のためのメール送信ページを表示
    @GetMapping("createaccount/mail")
    public String mail(){
        return "account/createaccountone";
    }

    //メールを実際に送る処理
    @PostMapping("sendmail/mail")
    public String sendMail(@RequestParam String mail){
        //メールを送る処理　実装不可
        return "account/createaccountone";
    }

    //アカウント作成するページを表示
    @GetMapping("createaccount/create")
    public String account(@RequestParam("email") String email, Model model){
        //クエリパラメタでメールアドレスを受けとる予定
        model.addAttribute("email", email);
        return "account/createaccounttwo";
    }

    //アカウント登録機能
    @PostMapping("createaccount/create")
    public String createAccount(@ModelAttribute account account,@RequestParam String password,Model model){
        //パスワードが一致しない
        if(!account.getPassword().equals(password)){
            model.addAttribute("not", "パスワードが一致しません");
            return "account/createaccounttwo";
        }
        userService.createAccount(account);
        model.addAttribute("clear","登録しました");
        return "account/createaccounttwo";
    }

    //ログインページを表示
    @GetMapping("login")
    public String login(){
        return "account/login";
    }

    //ログインパスワードを比較
    @PostMapping("login/password")
    public String loginPassword(@RequestParam String name,@RequestParam String password,HttpSession session, Model model){
        Map<String, Object> result = userService.readPassword(name, password);

        boolean checkpassword = result != null && (boolean) result.get("passwordMatch");
        Long id = result != null ? (Long) result.get("id") : null;

        if(checkpassword&&id!=null){
            session.setAttribute("id", id);
            model.addAttribute("logi", "ログイン成功しました");
            return "redirect:quiz/stamp";//普通に再度ページを表示のほうがいいかな？
        }else{
            model.addAttribute("failure", "パスワードもしくはユーザーネームが間違っています");
            return "account/login";
        }      
    }
    //ログアウト

    //パスワードを忘れたページを表示
    @GetMapping("login/forgotpassword")
    public String passwordmail(){
        return "account/forgotpassone";
    }

    //パスワードを変更するためのメールを送る
    @PostMapping("login/forgotpassword")
    public String readmail(){
        //メールを送る処理。上と同様
        return "account/forgotpasswordone";
    }

    //パスワードを変更するクエリパラメタでメールアドレスを受け取る予定
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

    //アカウント完全削除 確認まだ
    @PostMapping("deleteaccount")
    public String deleteAccount(HttpSession session){
       Object obj = session.getAttribute("id");
       Long id = (Long)obj;
       userService.deleteAccount(id);
       return "nopage";
    }

    //スタンプを表示　確認まだ
    @SuppressWarnings("unchecked")
    @GetMapping("stamp")
    public String getStamp(HttpSession session,Model model){
        
        if(session.getAttribute("id")==null){
            return "nopage";
        }
        Object obj = session.getAttribute("id");
       Long id = (Long)obj;
       int count = userService.getCount(id);
        model.addAttribute("count", count);
        Set<Integer> correct = (Set<Integer>) session.getAttribute("correct");
        model.addAttribute("correct", correct);
        return "stamp";
    }

    //テスト用 必要ない消して
    @GetMapping("/")
    public String test() {
        return "admin/admin";
    }
    

    //報酬を表示　　確認まだ
    @GetMapping("stamp/reward")
    public String reward(Model model,HttpSession session){
        if(session.getAttribute("id")==null){
            return "nopage";
        }
        Object obj = session.getAttribute("id");
       Long id = (Long)obj;
        model.addAttribute("count",userService.getCount(id) ); 
        return "reward";

    }

    //ランキングを表示　確認まだ
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
