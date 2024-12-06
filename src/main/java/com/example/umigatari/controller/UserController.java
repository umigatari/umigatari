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

import com.example.umigatari.NotFoundException;
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
    @PostMapping("sendmail")
    public String sendMail(@RequestParam String mail,Model model){
        //メールを送る処理　
        model.addAttribute("send", "メールを送信しました");
        return "account/createaccountone";
    }

    //アカウント作成するページを表示
    @GetMapping("createaccount/create")
    public String account(@RequestParam("mail") String mail, Model model){
        //クエリパラメタでメールアドレスを受けとる予定
        model.addAttribute("mail", mail);
        return "account/createaccounttwo";
    }

    //アカウント登録機能
    @PostMapping("create")
    public String createAccount(@ModelAttribute account account, @RequestParam String password, Model model) {
    try {
        userService.createAccount(account);
        model.addAttribute("clear", "登録しました");
    } catch (NotFoundException e) {
        model.addAttribute("error", e.getMessage());
    }
    return "account/createaccounttwo";
    }


    //ログインページを表示
    @GetMapping("login")
    public String login(){
        return "account/login";
    }

    //ログインパスワードを比較
    @PostMapping("password")
    public String loginPassword(@RequestParam String name,@RequestParam String password,HttpSession session, Model model){
        Map<String, Object> result = userService.readPassword(name, password);

        boolean checkpassword = result != null && (boolean) result.get("passwordMatch");
        Long id = result != null ? (Long) result.get("id") : null;

        if(checkpassword&&id!=null){
            session.setAttribute("id", id);
            model.addAttribute("login", "ログイン成功しました");
            return "redirect:stamp";//普通に再度ページを表示のほうがいいかな？
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
    @PostMapping("forgotpassword")
    public String readmail(@RequestParam String mail,Model model){
        //メールを送る処理。上と同様
        model.addAttribute("send", "メールを送信しました");
        return "account/forgotpassone";
    }

    //パスワードを変更するクエリパラメタでメールアドレスを受け取る予定
    @GetMapping("changepassword")
    public String password(@RequestParam(value = "mail", required = true) String mail, Model model) {
    if (mail == null || mail.isBlank()) {
        model.addAttribute("error", "メールアドレスが指定されていません。");
        return "account/error"; // 適切なエラーページに遷移
    }

    model.addAttribute("mail", mail);
    return "account/forgotpasstwo";
}


   /*  @PostMapping("change")
    public String changePassword(){
        userService.updatePassword();
        return "redirect:login";
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
        model.addAttribute("account", userService.getName(id));
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
