package com.example.umigatari.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.umigatari.NotFoundException;
import com.example.umigatari.model.account;
import com.example.umigatari.repository.UserRepository;
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    //アカウント作成
    public void createAccount(account account) {
        try {
            String hashedPassword = passwordEncoder.encode(account.getPassword());
            account.setPassword(hashedPassword);

            userRepository.createAccount(account);

        } catch (DuplicateKeyException e) {
            throw new NotFoundException("そのユーザーネームは既に登録されています。別のユーザーネームを使用してください。");
        }
    }

    //ランキングを表示
    public Map<String,Object> ranking(int limit,Long id){
        List<account> rankingList = userRepository.rankingAccount(limit);
        int myRanking = userRepository.myRanking(id);
        Map<String,Object> result = new HashMap<>();
        result.put("rankingList",rankingList);
        result.put("myRankig",myRanking);
        return result;
    }

    //パスワードを返す
    public String loginAccount(String name){
        return userRepository.readPassword(name);
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

    //正答数を増やす
    public void countUp(Long id){
        userRepository.countUp(id);
    }

    //ログイン全般
    public Map<String, Object> readPassword(String name, String password) {
        Map<String, Object> result = new HashMap<>();
        // ユーザーIDを取得
        Long id = userRepository.redId(name);
        if (id == null) {
            // ユーザーIDがnullの場合（ユーザーが存在しない）
            result.put("id",null);
            result.put("passwordMatch", false);
            return result; 
        }
    
        // パスワードを取得
        String storedPassword = userRepository.readPassword(name);
        if (storedPassword == null) {
            // パスワードがnullの場合（ユーザーが存在しない）
            result.put("id",null);
            result.put("passwordMatch", false);
            return result; 
        }
    
        // パスワードを比較
        boolean passwordMatch = passwordEncoder.matches(password, storedPassword);
    
        // 結果をMapに格納
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
    
    

    
}
