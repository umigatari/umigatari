package com.example.umigatari.controller;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.umigatari.NotFoundException;
import com.example.umigatari.model.account;
import com.example.umigatari.service.AnalysisService;
import com.example.umigatari.service.UserService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;


@Controller

public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private AnalysisService analysisService;

    //トップページ
    @GetMapping("/")
    public String umigatari(Model model) {
        model.addAttribute("showPopup",true);
        return "userpage/entrance";
    }

    //アンケート結果を設定
    @PostMapping("addrivute")
    public String addrivute(Model model,HttpSession session,@RequestParam int answer){
        model.addAttribute("showPopup",false);
        session.setAttribute("addrivute",answer);
        return  "userpage/entrance";
    }

    //途中参加
    @GetMapping("midway")
    public String midway(Model model,HttpSession session) {
        model.addAttribute("showPopup",false);
        session.setAttribute("addrivute",0);//途中参加者は属性を0にする
        return "userpage/entrance";
    }


    //かえり
    @GetMapping("exit")
    public String seeyousoon(HttpSession session,Model model){
        //退館時間の記録
        Object obj = session.getAttribute("id");
        Long id = (Long) obj;
        Object objtime = session.getAttribute("entertime");
        Timestamp entertime = (Timestamp) objtime;
        analysisService.exitTime(id,entertime);
        String [] img = {"1","4","8","5","3"};
        Random random = new Random();
        int randomNumber = random.nextInt(5);
        model.addAttribute("img",img[randomNumber]);
        session.invalidate(); 
        return "userpage/exit";
    }
    
    //アカウント作成のためのメール送信ページを表示 ok
    @GetMapping("createaccount/mail")
    public String mail(){
        return "account/createaccountone";
    }

    //メールを実際に送る処理 ok
    @PostMapping("sendmail")
    public String sendMail(@RequestParam String mail,Model model,HttpSession session){
        boolean check = userService.chekMail(mail);
        Object objadd = session.getAttribute("addrivute");
        int addrivute = (int) objadd;
        if(check){
            String baseUrl = "https://umigatari-quiz.com/createaccount/create";
            String registrationUrl = baseUrl + "?mail=" + mail + "&addrvute=" + addrivute;
            String body = "新規登録用URLを送付しました。以下のURLから新規登録してください。\n"+registrationUrl;
            userService.sendEmail(mail, "新規登録について", body);
        model.addAttribute("send", "メールを送信しました");
        return "account/createaccountone";
        }
        model.addAttribute("failure", "メールが送信されませんでした。すでに利用されている可能性があります。");
        return "account/createaccountone";
    }

    //アカウント作成するページを表示 ok
    @GetMapping("createaccount/create")
    public String account(@RequestParam("mail") String mail, Model model,@RequestParam("addrvute") String addrivute){
        //クエリパラメタでメールアドレス、属性を受けとる
        model.addAttribute("addrivute", addrivute);
        model.addAttribute("mail", mail);
        return "account/createaccounttwo";
    }

    //アカウント登録機能 ok
    @PostMapping("create")
    public String createAccount(@RequestParam String mail,@RequestParam String name,@RequestParam int addrivute, @RequestParam String password, Model model,HttpSession session) {
    try {
        account account = new account();
        session.setAttribute("addrivute", addrivute);
       account.setName(name);
       account.setAddrivute(addrivute);
       account.setMail(mail);
       account.setPassword(password);
        userService.createAccount(account);
        model.addAttribute("clear", "登録しました");
    } catch (NotFoundException e) {
        model.addAttribute("addrivute", addrivute);
        model.addAttribute("mail", mail);
        model.addAttribute("error", e.getMessage());
    }
    return "account/createaccounttwo";
    }

    //ログインページを表示 ok
    @GetMapping("login")
    public String login(){
        return "account/login";
    }

    @PostMapping("password")
    public String loginPassword(@RequestParam String mail, @RequestParam String password, HttpSession session, Model model, HttpServletResponse response,HttpServletRequest request) {
        try {
            String name = userService.mailToName(mail);
            Map<String, Object> result = userService.readPassword(name, password);
            boolean checkpassword = result != null && (boolean) result.get("passwordMatch");
            Long id = result != null ? (Long) result.get("id") : null;
    
            if (checkpassword && id != null) {
                // 入館時間の記録
                long currentMillis = System.currentTimeMillis();
                Timestamp entertime = new Timestamp(currentMillis);
                session.setAttribute("entertime", entertime);
    
                Object objadd = session.getAttribute("addrivute");
                int addrivute = (int) objadd;
    
                analysisService.enterTime(id, addrivute, entertime);
                session.setAttribute("id", id);
                model.addAttribute("login", "ログイン成功しました");
    
                // 日本時間のタイムゾーンを指定
                ZoneId zoneId = ZoneId.of("Asia/Tokyo");

                // 現在の日本時間
                LocalDateTime now = LocalDateTime.now(zoneId);

                // 今日の23:59:59（日本時間）
                LocalDateTime endOfDay = now.toLocalDate().atTime(23, 59, 59);

                // 2つの時刻の差を秒で計算
                long secondsUntilEndOfDay = java.time.Duration.between(now, endOfDay).getSeconds();

                // セッションIDのクッキーを作成
                Cookie sessionCookie = new Cookie("JSESSIONID", session.getId());
                sessionCookie.setMaxAge((int) secondsUntilEndOfDay); // 今日の終わりまで有効
                sessionCookie.setPath("/");
                sessionCookie.setHttpOnly(true);
                response.addCookie(sessionCookie);

    
                

                Set<Integer> solvedQuizzes = userService.solvedquiz(id);
                session.setAttribute("solvedQuizzes", solvedQuizzes);
                
                if(solvedQuizzes.isEmpty()){
                    Set<Integer> correct = new LinkedHashSet<>();
                    correct.add(0);
                    session.setAttribute("correct", correct);   
                }else{
                    Set<Integer> correct = userService.answerquiz(id);
                    correct.add(0);
                session.setAttribute("correct", correct);
                }
                Cookie[] cookies = request.getCookies();
                if (cookies != null) {
                    for (Cookie cookie : cookies) {
                        if ("created".equals(cookie.getName()) && "true".equals(cookie.getValue())) {
                            session.setAttribute("created", true);
                            break;
                        }
                    }
                }

                return "redirect:stamp";
            } else {
                model.addAttribute("failure", "メールアドレスもしくはパスワードが間違っています");
                return "account/login";
            }
        } catch (NullPointerException e) {
            model.addAttribute("error", "セッションタイムアウト、または不正なアクセスです。再度ログインしてください。");
            return "error/timeout";
          } catch (Exception e) {
            model.addAttribute("error", "予期しないエラーが発生しました。管理者にお問い合わせください。");
            return "error/timeout"; 
        }
    
}
    

    //ログアウト ok
    @PostMapping("logout")
    public String logout(HttpSession session, HttpServletResponse response) {
        Long obj = 19L; // 管理者のID
    
        if (Objects.equals(session.getAttribute("id"), obj)) {
            session.invalidate();
            return "admin/adminlogin";
        }
    
        boolean check = false; // ここで最初に宣言しておく
    
        if (session.getAttribute("created") != null) {
            check = true; // ここで値を更新
        }
    
        session.invalidate(); 
    
        if (check) {
    // 日本時間（JST）の現在時刻
    LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Tokyo"));

    // 今日の23:59:59 JST
    LocalDateTime endOfDay = now.toLocalDate().atTime(23, 59, 59);

    // 今から今日の23:59:59までの秒数を計算
    long secondsUntilEndOfDay = ChronoUnit.SECONDS.between(now, endOfDay);

    // クッキーを作成し、今日の23:59:59まで有効にする
    Cookie createdCookie = new Cookie("created", "true");
    createdCookie.setMaxAge((int) secondsUntilEndOfDay); // 日本時間の今日中まで
    createdCookie.setPath("/"); // どのページでもアクセス可能にする
    response.addCookie(createdCookie);
}
    
        return "redirect:quiz/1";
    }

    

    //パスワードを忘れたページを表示 ok
    @GetMapping("login/forgotpassword")
    public String passwordmail(){
        return "account/forgotpassone";
    }

    //パスワードを変更するためのメールを送る 未実装
    @PostMapping("forgotpassword")
    public String readmail(@RequestParam String mail,Model model,HttpSession session){
        Object objadd = session.getAttribute("addrivute");
        int addrivute = (int) objadd;
        String baseUrl = "https://umigatari-quiz.com/changepassword";
            String registrationUrl = baseUrl + "?mail=" + mail + "&addrivute=" + addrivute;
            String body = "パスワード変更用URLを送付しました。以下のURLから変更してください。\n"+registrationUrl;
            userService.sendEmail(mail, "パスワード変更", body);
        model.addAttribute("send", "メールを送信しました");
        return "account/forgotpassone";
    }

    //パスワードを変更画面を表示
    @GetMapping("changepassword")
    public String password(@RequestParam(value = "mail") String mail,@RequestParam(value = "addrivute") String addrivute, Model model,HttpSession session) {
        //クエリパラメタから属性とメールアドレスを受け取る
    if (mail == null || mail.isBlank()) {
        model.addAttribute("error", "メールアドレスが指定されていません。");
        return "account/error"; // 適切なエラーページに遷移
    }
    model.addAttribute("mail", mail);
    model.addAttribute("addrivute", addrivute);
    return "account/forgotpasstwo";
    }


    //パスワード変更
    @PostMapping("change")
    public String changePassword(@RequestParam String password,@RequestParam String mail,Model model,@RequestParam int addrivute,HttpSession session){
        userService.changePassword(mail, password);
        model.addAttribute("clear", "登録しました");
        session.setAttribute("addrivute", addrivute);
        return "account/forgotpasstwo";
    }

    //アカウント完全削除
    @GetMapping("deleteaccount")
    public String deleteAccount(HttpSession session){
       Object obj = session.getAttribute("id");
       Long id = (Long)obj;
       session.invalidate(); 
       userService.deleteAccount(id);
       return "redirect:/";
    }

    //以下ユーザーページ

    // スタンプを表示
    @SuppressWarnings("unchecked")
    @GetMapping("stamp")
    public String getStamp(HttpSession session, Model model) {
        try {
            // セッションIDの確認
            Object obj = session.getAttribute("id");
            if (obj == null) {
                return "userpage/nopage";
            }

            Long id = (Long) obj;
            Set<Integer> solvedQuizzes = (Set<Integer>) session.getAttribute("solvedQuizzes");
            if (solvedQuizzes == null) {
                model.addAttribute("solvedCount", 5);
            } else {
                int solvedCount = 5 - solvedQuizzes.size();
                model.addAttribute("solvedCount", solvedCount);
            }

            int count = userService.getCount(id);
            model.addAttribute("count", count);

            Set<Integer> correct = (Set<Integer>) session.getAttribute("correct");
            model.addAttribute("correct", correct != null ? correct : Set.of());

            model.addAttribute("account", userService.getName(id));
            return "userpage/stamp";
        } catch (Exception e) {
            // 予期しないエラーが発生した場合もタイムアウト画面へ遷移
            return "error/timeout";
        }
    }

    //報酬を表示　　
    @GetMapping("reward")
    public String reward(Model model,HttpSession session,HttpServletRequest request){
        if(session.getAttribute("id")==null){
            return "userpage/nopage";
        }

        Object obj = session.getAttribute("id");
       Long id = (Long)obj;
       model.addAttribute("account", userService.getName(id));
        model.addAttribute("count",userService.getCount(id) ); 
        return "userpage/reward";

    }

    //ランキングを表示　
    @SuppressWarnings("unchecked")
    @GetMapping("ranking")
    public String ranking(HttpSession session, Model model,HttpServletRequest request){
        if(session.getAttribute("id")==null){
            return "userpage/nopage";
        }

        int limit = 5;
        Object obj = session.getAttribute("id");
       Long id = (Long)obj;
        Map<String,Object> result=userService.ranking(limit,id);
        List<account> ranking =  (List<account>) result.get("rankingList");
        int myRanking= (int) result.get("myRanking");
        model.addAttribute("account", userService.getName(id));
        model.addAttribute("ranking", ranking);
        model.addAttribute("myranking", myRanking);
        return "userpage/ranking";
    }
    
    //二次元コードを読み取る
    @GetMapping("qr")
    public String qrcode(HttpSession session,HttpServletRequest request){
        if(session.getAttribute("id")==null){
            return "userpage/nopage";
        }
        String referer = request.getHeader("Referer");
        String allowedRefererPattern = "^https?://umigatari-quiz.com/stamp.*";
        if (referer == null || !referer.matches(allowedRefererPattern)) {
            if (referer == null) {
                return "redirect:/userpage/nopage";
            }
            return "redirect:" + referer;
        }
        return"userpage/qr";
    }

    
    
}

/*String referer = request.getHeader("Referer");
        String allowedRefererPattern = "^https?://examplepj.f5.si/stamp.*";
        if (referer == null || !referer.matches(allowedRefererPattern)) {
            if (referer == null) {
                return "redirect:/userpage/nopage";
            }
            return "redirect:" + referer;
        } */