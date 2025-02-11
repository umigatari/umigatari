package com.example.umigatari.controller;


import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.umigatari.NotFoundException;
import com.example.umigatari.model.analysis;
import com.example.umigatari.model.answer;
import com.example.umigatari.model.choice;
import com.example.umigatari.model.quiz;
import com.example.umigatari.model.rmdquiz;
import com.example.umigatari.service.AnalysisService;
import com.example.umigatari.service.QuizService;
import com.example.umigatari.service.UserService;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller

public class QuizController {

    @Autowired
    private QuizService quizService;

    @Autowired
    private UserService userService;

    @Autowired
    private AnalysisService analysisService;


    //QRコードごとのクイズを表示する
    @SuppressWarnings("unchecked")
    @GetMapping("quiz/{type}")
    public String randomThreeQuiz(@PathVariable int type, Model model,HttpSession session) {
        //ログインしているか判断
        if(session.getAttribute("id")==null){
            return "userpage/nopage";
        }
        Set<Integer> solvedQuizzes = (Set<Integer>) session.getAttribute("solvedQuizzes");
        if (solvedQuizzes == null) {
            solvedQuizzes = new LinkedHashSet<>();
        }
        //すでに解いているか判断。解いていれば、問題は表示されない。
        if (!solvedQuizzes.contains(type)) {  
            //これがあるからクイズがリロードすると溶けなくなる
            //solvedQuizzes.add(type);
            //session.setAttribute("solvedQuizzes", solvedQuizzes);
            //クイズを取得
            List<quiz> quiz = quizService.randomThreeQuiz();
            //クイズを振り分け
            rmdquiz quiz1 = new rmdquiz();
            quiz1.setId(quiz.get(0).getId());
            quiz1.setQuestion(quiz.get(0).getQuestion());
    
            rmdquiz quiz2 = new rmdquiz();
            quiz2.setId(quiz.get(1).getId());
            quiz2.setQuestion(quiz.get(1).getQuestion());
    
            rmdquiz quiz3 = new rmdquiz();
            quiz3.setId(quiz.get(2).getId());
            quiz3.setQuestion(quiz.get(2).getQuestion());

            model.addAttribute("quiz1", quiz1);
            model.addAttribute("quiz2", quiz2);
            model.addAttribute("quiz3", quiz3);
            model.addAttribute("type",type);
        } else {
            model.addAttribute("soved", "すでに問題を解いています");
        }
        return "quiz/randomquiz";
    }

    //ひとつ選ばれたクイズを表示する
    @PostMapping("quiz/{id}")
    public String showOneQuiz(@PathVariable("id") Long id,Model model,HttpSession session,@RequestParam int type,HttpServletRequest request) {
        //ログインか判断
        if(session.getAttribute("id")==null){
            return "userpage/nopage";
        }

        //クイズを取得
        quiz quiz = quizService.selectOneById(id);

        //選択肢をシャッフル
        List<String> choices = new ArrayList<>(List.of(quiz.getCorrect(), quiz.getOther_one(), quiz.getOther_two()));
        Collections.shuffle(choices);
        
        // 解答をセット
        choice choice1 = new choice();
        choice1.setChoice(choices.get(0));

        choice choice2 = new choice();
        choice2.setChoice(choices.get(1));
        
        choice choice3 = new choice();
        choice3.setChoice(choices.get(2));

        model.addAttribute("quiz", quiz);
        model.addAttribute("type",type);
        model.addAttribute("choice1", choice1);
        model.addAttribute("choice2", choice2);
        model.addAttribute("choice3", choice3);
        

        //セッションに回答をセットセット
        session.setAttribute("answer", quiz.getCorrect());

        return "quiz/quiz";
    }

    //クイズの回答を表示する
    @SuppressWarnings("unchecked")
    @PostMapping("quiz/answer")
    public String check(@RequestParam String choice,@RequestParam int type, @RequestParam Long id,Model model,HttpSession session,HttpServletRequest request) {
        //ログインしてないなら返す
        if(session.getAttribute("id")==null){
            return "userpage/nopage";
        }
        
        Set<Integer> solvedQuizzes = (Set<Integer>) session.getAttribute("solvedQuizzes");
        if (solvedQuizzes == null) {
            solvedQuizzes = new LinkedHashSet<>();
        }
        solvedQuizzes.add(type);
        session.setAttribute("solvedQuizzes", solvedQuizzes);

        //正解を取得
        Object answerobj =session.getAttribute("answer");
        String answer = (String) answerobj;

        //正解数を取得
       Object obj = session.getAttribute("id");
       Long uid = (Long)obj;
        int count = userService.getCount(uid);

        //問題を取得
        quiz quiz = quizService.selectOneById(id);
        answer qanda = new answer();
        qanda.setQuiz(quiz.getQuestion());
        qanda.setAnswer(quiz.getCorrect());

        //次の問題へのメッセージ

        switch (type) {
            case 1 -> model.addAttribute("nextmessage", "次は○○に移動してね");
            case 2 -> model.addAttribute("nextmessage", "次は○○で問題が解けるよ");
            case 3 -> model.addAttribute("nextmessage", "次は○○だよ");
            case 4 -> model.addAttribute("nextmessage", "次が最後の場所だよ");
                default -> model.addAttribute("nextmessage", "お疲れ様。帰りのQR読み込んで画像ゲットしてね");
            }


        //正解かどうかを判断
        if (answer.equals(choice)) {
            String str = "今までの累計正解数は、" + count + "回です";
            model.addAttribute("count", str);
            userService.countUp(id);
            model.addAttribute("message", "正解!");
            Set<Integer> correct = (Set<Integer>) session.getAttribute("correct");
            System.out.println(correct);
            correct.add(type);
            session.setAttribute("correct", correct);
            model.addAttribute("str",str);
            model.addAttribute("qanda",qanda);
            model.addAttribute("correct",correct);
        //不正解なら不正解と表示
        } else {
            model.addAttribute("message", "不正解");
            Set<Integer> correct = (Set<Integer>) session.getAttribute("correct");
            System.out.println(correct);
            String str = "今までの累計正解数は、" + count + "回です";
            model.addAttribute("str",str);
            model.addAttribute("qanda",qanda);
            model.addAttribute("correct",correct);
        }

        return "quiz/answer";
    }

    //クイズ作成のページを表示
    @GetMapping("quiz/createpage")
    public String createQuizPage(HttpSession session, Model model,HttpServletRequest request) {
        //ログインしているか判断
        if(session.getAttribute("id")==null){
            return "userpage/nopage";
        }

        //リファラで遷移が正しいかチェック
        String referer = request.getHeader("Referer");
        String allowedRefererPattern = "^https?://localhost:8080.*";
        if (referer == null || !referer.matches(allowedRefererPattern)) {
            if (referer == null) {
                return "redirect:/userpage/nopage";
            }
            return "redirect:" + referer;
        }

        //正解数を取得
        Object obj = session.getAttribute("id");
        Long id = (Long)obj;
        int count = userService.getCount(id);
        model.addAttribute("count", count);
        return "quiz/adduserquiz";
    }

    //クイズ作成機能
    @PostMapping("quiz/create")
    public String createQuiz(@ModelAttribute quiz quiz, Model model, HttpSession session) {
        boolean ngword = quizService.isTextValid(quiz.getQuestion());
        if(ngword){
            quizService.insertQuiz(quiz);
            model.addAttribute("create", "問題を作成しました!");
        }else{
        model.addAttribute("ngword", "不適切な問題です。");
        }
        return "quiz/adduserquiz";
    }

    //ルールを表示
    @GetMapping("rule")
    public String getRule(HttpServletRequest request,HttpSession session,Model model) {

         Object obj = session.getAttribute("id");
       Long id = (Long)obj;
       model.addAttribute("account", userService.getName(id));
        return "userpage/rule";
    }

    //以下管理者ページ

    //管理者のログインページを作成
    @GetMapping("adminlogin")
    public String adminLogin(){
        return "admin/adminlogin";
    }

    //管理者のパスワード比較
    @PostMapping("adminpassword")
    public String adminLoginPassword(@RequestParam String name,@RequestParam String password,HttpSession session, Model model){
         Map<String, Object> result = userService.readPassword(name, password);
        boolean checkpassword = result != null && (boolean) result.get("passwordMatch");
        Long id = result != null ? (Long) result.get("id") : null;
        if(checkpassword&&id!=null){
            //管理者アカウントなら管理者画面に遷移
            if(name.equals("admin")){
                session.setAttribute("id", id);
                return "redirect:admin";
            }else{
                return "admin/adminlogin";
            }
        }else{
            model.addAttribute("failure", "パスワードもしくはユーザーネームが間違っています");
            return "admin/adminlogin";
        }      
    }
    
    //adminページを表示
    @GetMapping("admin")
    public String admin(Model model,HttpSession session){
        Long obj = 19L;//管理者のIDが入る
        if(!Objects.equals(session.getAttribute("id"), obj)){
            return "admin/adminlogin";
        }
        int notice = quizService.getNotice();
        if(notice>=99){
            notice=99;
        }
        model.addAttribute("notice",notice);
        return "admin/admin";
    }

    //問題作成ページを表示
    @GetMapping("admin/create")
    public String adminCreatQuizPage(HttpSession session,Model model,HttpServletRequest request){
        Long obj = 19L;//管理者のIDが入る
        if(!Objects.equals(session.getAttribute("id"), obj)){
            return "admin/adminlogin";
        }
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

    //問題作成
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
            model.addAttribute("posting", "編集したのでこの問題を公開しますか？");
            model.addAttribute("id", quiz.getId());
        //一覧画面からの遷移の場合    
        }else{
        model.addAttribute("create", "更新しました!");}}
        return "admin/createquiz";
    }

    //問題チェック画面表示
    @GetMapping("admin/check")
    public String checkQuizPage(Model model,@RequestParam(value = "p_cocid", required = false) Integer pCocid,
    @RequestParam(value = "dord", required = false) String dord,HttpSession session,HttpServletRequest request){
        Long obj = 19L;//管理者のIDが入る
        if(!Objects.equals(session.getAttribute("id"), obj)){
            return "admin/adminlogin";
        }
        //チェックが必要なクイズを表示。なければな”問題はありません”と表示
        try {
            if (pCocid != null && dord != null) {
                // クエリパラメタに何もないとき
                List<quiz> quiz= quizService.readOrderTypeQuizCheck(dord,pCocid);
                model.addAttribute("quiz",quiz);
                return "admin/check";
            } else if (pCocid != null) {
                //このコード意味ない
                List<quiz> quiz= quizService.selectByTypeCheck(pCocid);
                model.addAttribute("quiz",quiz);
                return "admin/check";
            } else if (dord != null) {
                // 降順か昇順か判断
                List<quiz> quiz= quizService.selectByOrderCheck(dord);
                model.addAttribute("quiz",quiz);
                return "admin/check";
            } else {
                List<quiz> quizzes = quizService.checkListQuiz();
            model.addAttribute("quiz", quizzes);
            return "admin/check";
            }
            
        } catch (NotFoundException e) {
            model.addAttribute("errorMessage", "問題はありません");
            return "admin/check";
        }
    }

    //クイズ一覧
    @GetMapping("admin/quizlist")
    public String quizListPage(Model model,@RequestParam(value = "keyword", required = false) String keyword,
    @RequestParam(value = "dord", required = false) String dord,HttpSession session,HttpServletRequest request){
        Long obj = 19L;//管理者のIDが入る
        if(!Objects.equals(session.getAttribute("id"), obj)){
            return "admin/adminlogin";
        }
        
        if (keyword != null && dord != null) {
            // 両方のパラメータがある場合の処理
            List<quiz> quiz= quizService.serchBykeyword(keyword, dord);
            model.addAttribute("quiz",quiz);
            return "admin/quizlist";
        } else if (keyword != null) {
            // p_cocidのみがある場合の処理
            List<quiz> quiz= quizService.serchBykeyword(keyword,"ASC");
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

    //クイズ削除 問題チェック画面から
    @PostMapping("admin/delete")
    public String deleteQuiz(@RequestParam Long id, @RequestParam(required = false) String p_cocid, 
    @RequestParam(required = false) String dord){
        quizService.deleteQuiz(id);
        // 削除後にパラメータをリダイレクトURLに追加
    String redirectUrl = "redirect:/admin/check";
    if (p_cocid != null && !p_cocid.isEmpty()) {
        redirectUrl += "?p_cocid=" + p_cocid;
    }
    if (dord != null && !dord.isEmpty()) {
        redirectUrl += (redirectUrl.contains("?") ? "&" : "?") + "dord=" + dord;
    }

    return redirectUrl;
    }

    //クイズ削除 一覧から
    @PostMapping("admin/delete2")
    public String deleteQuiz2(@RequestParam Long id, @RequestParam(required = false) String p_cocid, 
                          @RequestParam(required = false) String dord) {
    quizService.deleteQuiz(id);
    
    // 削除後にパラメータをリダイレクトURLに追加
    String redirectUrl = "redirect:/admin/quizlist";
    if (p_cocid != null && !p_cocid.isEmpty()) {
        redirectUrl += "?p_cocid=" + p_cocid;
    }
    if (dord != null && !dord.isEmpty()) {
        redirectUrl += (redirectUrl.contains("?") ? "&" : "?") + "dord=" + dord;
    }

    return redirectUrl;
    }

    //チェック済みにする
    @PostMapping("admin/update")
    public String updateQuiz(@RequestParam Long id){
        quizService.updateConfirmation(id);
        return "redirect:/admin/check";
        //quizService.updateQuiz(quiz);
    }

    //チェックを解除する
    @PostMapping("admin/unpost")
    public String unpostQuiz(@RequestParam Long id){
        quizService.unpostConfirmation(id);
        return "redirect:/admin/quizlist";
        //quizService.updateQuiz(quiz);
    }

     //クイズ更新ページに遷移
    @PostMapping("/editing")
    public String editingQUiz(@RequestParam("id") long id, HttpSession session) {
        session.setAttribute("quizid", id);
        return "redirect:/admin/create";
    }

    //分析ページを表示
    @GetMapping("admin/analysis")
    public String analysis(Model model,HttpSession session,HttpServletRequest request) {
        Long obj = 19L;//管理者のIDが入る
        //管理者はもっと複雑にするべき
        if(!Objects.equals(session.getAttribute("id"), obj)){
            return "admin/adminlogin";
        }
        analysis analysis = new analysis();
        analysis.setStaytime(analysisService.updateStayTime());
        String [] arr = {"一人","家族","友人","恋人","その他","全体"};
        model.addAttribute("count",userService.getMember());
        model.addAttribute("analysis", analysis);
        model.addAttribute("attributes", arr);
        return "admin/analysis";
    }

    //CSVファイルダウンロード
    @GetMapping("/download-staytime")
    public ResponseEntity<Void> downloadStayTime(HttpServletResponse response) throws IOException {

        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"staytime.csv\"");
        response.setContentType("text/csv");

        try (ServletOutputStream outputStream = response.getOutputStream();
             OutputStreamWriter writer = new OutputStreamWriter(outputStream)) {
            analysisService.exportStayTimeToCsv(writer);
        }

        return ResponseEntity.ok().build();
    }
    
}
