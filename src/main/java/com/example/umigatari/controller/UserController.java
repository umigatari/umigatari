package com.example.umigatari.controller;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.HashMap;
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
import com.example.umigatari.service.AnalysisService;
import com.example.umigatari.service.UserService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/*属性をattributeなのをaddrivuteと勘違いしています。
 * 使用してるセッション
 * solvedQuizzes解いた問題
 * correct正解した問題
 * answer正解の情報
 * timeセクションごとの時間の情報
 * idユーザーID
 * addrivuteユーザの属性
 * entertime入館時間
 */
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
        return "userpage/umigatari";
    }

    //アンケート結果を設定
    @PostMapping("addrivute")
    public String addrivute(Model model,HttpSession session,@RequestParam int answer){
        model.addAttribute("showPopup",false);
        session.setAttribute("addrivute",answer);
        return  "userpage/umigatari";
    }

    //かえり
    @SuppressWarnings("unchecked")
    @GetMapping("seeyousoon")
    public String seeyousoon(HttpSession session){
        //退館時間の記録
        Object obj = session.getAttribute("id");
        Long id = (Long) obj;
        Object objtime = session.getAttribute("entertime");
        Timestamp entertime = (Timestamp) objtime;
        analysisService.exitTime(id,entertime);
        //セクションごとの時間の記録
        Map<String, Object> timeMap = (Map<String, Object>) session.getAttribute("time");
        if (timeMap != null) {
            Object objid = session.getAttribute("id");
            Long accountid = (Long)objid;
            Object objaddri = session.getAttribute("addrivute");
            int addrivute = (int)objaddri;
            int type = (int) timeMap.get("type");
            long timestamp2 = (Long) timeMap.get("timestamp");
            long timestamp = Instant.now().getEpochSecond();
            analysisService.addSectionTime(id,accountid,addrivute,type,6,timestamp,timestamp2);
        }
        return "userpage/exit";
    }
    
    //アカウント作成のためのメール送信ページを表示 ok
    @GetMapping("createaccount/mail")
    public String mail(){
        return "account/createaccountone";
    }

    //メールを実際に送る処理 未実装
    @PostMapping("sendmail")
    public String sendMail(@RequestParam String mail,Model model,HttpSession session){
        boolean check = userService.chekMail(mail);
        //Object objadd = session.getAttribute("addrivute");
        //int addrivute = (int) objadd;
        if(check){
            //メールを送る処理　
        model.addAttribute("send", "メールを送信しました");
        return "account/createaccountone";
        }
        model.addAttribute("failure", "そのアドレスは既に登録されています");
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
    public String createAccount(@ModelAttribute account account, @RequestParam String password, Model model,HttpSession session) {
    try {
        int addrivute = account.getAddrivute();
        session.setAttribute("addrivute", addrivute);
        userService.createAccount(account);
        model.addAttribute("clear", "登録しました");
    } catch (NotFoundException e) {
        String mail =account.getMail();
        int addrivute = account.getAddrivute();
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

    //ログインパスワードを比較 ok
    @PostMapping("password")
    public String loginPassword(@RequestParam String name,@RequestParam String password,HttpSession session, Model model,HttpServletResponse response){
        //メールアドレスに変更予定。今は、開発環境でメールアドレスうつのだるいんでユーザーネーム使います。変数名mailだけど開発中はnameが入ります。
        Map<String, Object> result = userService.readPassword(name, password);
        boolean checkpassword = result != null && (boolean) result.get("passwordMatch");
        Long id = result != null ? (Long) result.get("id") : null;
        if(checkpassword&&id!=null){
            //管理者アカウントなら管理者画面に遷移
            if(name.equals("admin")){
                session.setAttribute("id", id);
                return "redirect:admin";
            }
            //入館時間の記録
            long currentMillis = System.currentTimeMillis();
            Timestamp entertime = new Timestamp(currentMillis);
            session.setAttribute("entertime", entertime);
            Object objadd = session.getAttribute("addrivute");
            int addrivute = (int) objadd;
            analysisService.enterTime(id, addrivute,entertime);
            session.setAttribute("id", id);
            model.addAttribute("login", "ログイン成功しました");
             //時間の記録
            Map<String, Object> timeMap = new HashMap<>();
            long timestamp = Instant.now().getEpochSecond();
            timeMap.put("type", 0);
            timeMap.put("timestamp", timestamp);
            session.setAttribute("time", timeMap);
             // セッションIDのクッキーの有効期限を6時間に設定
            Cookie sessionCookie = new Cookie("JSESSIONID", session.getId());
            sessionCookie.setMaxAge(6 * 60 * 60); // 6時間
            sessionCookie.setPath("/");
            sessionCookie.setHttpOnly(true);
            response.addCookie(sessionCookie);

            return "redirect:stamp";
        }else{
            model.addAttribute("failure", "パスワードもしくはユーザーネームが間違っています");
            return "account/login";
        }      
    }

    //ログアウト ok
    @PostMapping("logout")
    public String logout(HttpSession session) {
        session.invalidate(); 
        return "redirect:";
    }
    

    //パスワードを忘れたページを表示 ok
    @GetMapping("login/forgotpassword")
    public String passwordmail(){
        return "account/forgotpassone";
    }

    //パスワードを変更するためのメールを送る 未実装
    @PostMapping("forgotpassword")
    public String readmail(@RequestParam String mail,Model model,HttpSession session){
        //Object objadd = session.getAttribute("addrivute");
        //int addrivute = (int) objadd;
        //メールを送る処理。上と同様
        model.addAttribute("send", "メールを送信しました");
        return "account/forgotpassone";
    }

    //パスワードを変更画面を表示
    @GetMapping("changepassword")
    public String password(@RequestParam(value = "mail", required = true) String mail,@RequestParam(value = "addrivute", required = true) String addrivute, Model model,HttpSession session) {
        //クエリパラメタから属性とメールアドレスを受け取る
        session.setAttribute("addrivute", addrivute);
    if (mail == null || mail.isBlank()) {
        model.addAttribute("error", "メールアドレスが指定されていません。");
        return "account/error"; // 適切なエラーページに遷移
    }
    model.addAttribute("mail", mail);
    return "account/forgotpasstwo";
    }


    //パスワード変更
    @PostMapping("change")
    public String changePassword(@RequestParam String password,@RequestParam String mail ){
        userService.changePassword(mail, password);
        return "redirect:login";
    }

    //アカウント完全削除
    @PostMapping("deleteaccount")
    public String deleteAccount(HttpSession session){
       Object obj = session.getAttribute("id");
       Long id = (Long)obj;
       session.invalidate(); 
       userService.deleteAccount(id);
       return "redirect:";
    }

    //以下ユーザーページ

    //スタンプを表示
    @SuppressWarnings("unchecked")
    @GetMapping("stamp")
    public String getStamp(HttpSession session,Model model){
        
        if(session.getAttribute("id")==null){
            return "userpage/nopage";
        }
        Object obj = session.getAttribute("id");
       Long id = (Long)obj;
       int count = userService.getCount(id);
        model.addAttribute("count", count);
        Set<Integer> correct = (Set<Integer>) session.getAttribute("correct");
        model.addAttribute("correct", correct);
        model.addAttribute("account", userService.getName(id));
        return "userpage/stamp";
    }

    //報酬を表示　　
    @GetMapping("reward")
    public String reward(Model model,HttpSession session,HttpServletRequest request){
        if(session.getAttribute("id")==null){
            return "userpage/nopage";
        }
        //リファラで遷移が正しいかチェック
        String referer = request.getHeader("Referer");
        String allowedRefererPattern = "^https?://localhost:8080/stamp";
        if (referer == null || !referer.matches(allowedRefererPattern)) {
            if (referer == null) {
                return "redirect:/userpage/nopage";
            }
            return "redirect:" + referer;
        }
        Object obj = session.getAttribute("id");
       Long id = (Long)obj;
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
        String referer = request.getHeader("Referer");
        String allowedRefererPattern = "^https?://localhost:8080/stamp";
        if (referer == null || !referer.matches(allowedRefererPattern)) {
            if (referer == null) {
                return "redirect:/userpage/nopage";
            }
            return "redirect:" + referer;
        }
        int limit = 5;
        Object obj = session.getAttribute("id");
       Long id = (Long)obj;
        Map<String,Object> result=userService.ranking(limit,id);
        List<account> ranking =  (List<account>) result.get("rankingList");
        int myRanking= (int) result.get("myRanking");
        model.addAttribute("ranking", ranking);
        model.addAttribute("myranking", myRanking);
        return "userpage/ranking";
    }
    
    @GetMapping("qr")
    public String qrcode(HttpSession session,HttpServletRequest request){
        if(session.getAttribute("id")==null){
            return "userpage/nopage";
        }
        String referer = request.getHeader("Referer");
        String allowedRefererPattern = "^https?://localhost:8080/stamp";
        if (referer == null || !referer.matches(allowedRefererPattern)) {
            if (referer == null) {
                return "redirect:/userpage/nopage";
            }
            return "redirect:" + referer;
        }
        return"userpage/QR";
    }
    
}
