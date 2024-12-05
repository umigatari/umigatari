package com.example.umigatari.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
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
    public void createAccount(account account){
        String hashedPassword = passwordEncoder.encode(account.getPassword());
        account.setPassword(hashedPassword);
        userRepository.createAccount(account);
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
    public void changePassword(String name,String password){
        userRepository.updatePassword(name,password);
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
        try {
            Long id = userRepository.redId(name);
            String storedPassword = userRepository.readPassword(name);
    
            if (storedPassword == null) {
                throw new NotFoundException("ユーザーが見つからない: " + name);
            }
    
            boolean passwordMatch = passwordEncoder.encode(password).equals(storedPassword);
            result.put("id", id);
            result.put("password", passwordMatch);
    
            return result;
        } catch (NotFoundException e) {
            return null;
        }
    }

    //正答数を表示
    public int getCount(Long id){
        return userRepository.getCount(id);
    }
    
    

    
}
