package com.example.umigatari.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.umigatari.model.account;
import com.example.umigatari.repository.UserRepository;

import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.Body;
import software.amazon.awssdk.services.ses.model.Content;
import software.amazon.awssdk.services.ses.model.Destination;
import software.amazon.awssdk.services.ses.model.Message;
import software.amazon.awssdk.services.ses.model.SendEmailRequest;
import software.amazon.awssdk.services.ses.model.SesException;
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    private final SesClient sesClient;
    
    //sesClientの初期化
    public UserService(@Value("${aws.region}") String region) {
        this.sesClient = SesClient.builder()
                .region(Region.of(region))
                .credentialsProvider(DefaultCredentialsProvider.create()) 
                .build();
    }

    //メールを送信資する処理
    public void sendEmail(String mail, String subject, String body) {
        try {
            SendEmailRequest emailRequest = SendEmailRequest.builder()
                    .destination(Destination.builder().toAddresses(mail).build())
                    .message(Message.builder()
                            .subject(Content.builder().data(subject).charset("UTF-8").build())
                            .body(Body.builder()
                                    .text(Content.builder().data(body).charset("UTF-8").build())
                                    .build())
                            .build())
                    .source("your-verified-email@example.com")//アドレスは後で変更
                    .build();

            sesClient.sendEmail(emailRequest);
        } catch (SesException e) {
            System.err.println("Email sending failed: " + e.awsErrorDetails().errorMessage());
            throw new RuntimeException("Failed to send email", e);
        }
    }

    //アカウント作成
    public void createAccount(account account) {
        //try {
            String hashedPassword = passwordEncoder.encode(account.getPassword());
            account.setPassword(hashedPassword);

            userRepository.createAccount(account);

        /* } catch (DuplicateKeyException e) {
            throw new NotFoundException("そのユーザーネームは既に登録されています。別のユーザーネームを使用してください。");
        }*/
    }

    //メールアドレスチェック
    public boolean chekMail(String mail){
        int count = userRepository.checkMail(mail);
        return count == 0;
    }


    //ランキングを表示
    public Map<String,Object> ranking(int limit,Long id){
        List<account> rankingList = userRepository.rankingAccount(limit);
        int myRanking = userRepository.myRanking(id);
        Map<String,Object> result = new HashMap<>();
        result.put("rankingList",rankingList);
        result.put("myRanking", myRanking);
        return result;
    }

    //パスワードを返す
    public String loginAccount(String name){
        return userRepository.readPasswordByname(name);
    }

    //パスワードを変更
    public void changePassword(String mail,String password){
        String hashedPasseord = passwordEncoder.encode(password);
        userRepository.updatePassword(mail,hashedPasseord);
    }

    //アカウント完全削除
    public void deleteAccount(Long id){
        userRepository.deleteAccount(id);
    }

    //メールアドレスを表示
    public String readMail(String name){
        return userRepository.readMail(name);
    }
    //メールアドレスから名前を取得
    public String mailToName(String mail){
        return  userRepository.mailToName(mail);
    }

    //正答数を増やす
    public void countUp(Long id){
        userRepository.countUp(id);
    }

    //ログイン全般
    public Map<String, Object> readPassword(String name, String password) {
        Map<String, Object> result = new HashMap<>();
        // ユーザーIDを取得
        //Long id = userRepository.redIdBymail(mail);
        Long id = userRepository.redIdByname(name);
        if (id == null) {
            //ユーザーが存在しない
            result.put("id",null);
            result.put("passwordMatch", false);
            return result; 
        }
    
        // パスワードを取得
        //String storedPassword = userRepository.readPasswordBymail(mail);
        String storedPassword = userRepository.readPasswordByname(name);
        if (storedPassword == null) {
            //ユーザーが存在しない
            result.put("id",null);
            result.put("passwordMatch", false);
            return result; 
        }
        boolean passwordMatch = passwordEncoder.matches(password, storedPassword);
        result.put("id", id);
        result.put("passwordMatch", passwordMatch);
    
        return result;
    }

    //正答数を表示
    public int getCount(Long id){
        return userRepository.getCount(id);
    }

    //IDに対するユーザーネームの表示
    public String getName(Long id){
        return userRepository.getName(id);
    }   

    //登録した人数を数える
    public int getMember(){
        return userRepository.getMember();
    }
}
