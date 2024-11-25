package com.example.umigatari.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.umigatari.model.account;
import com.example.umigatari.repository.UserRepository;
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public void createAccount(account account){
        userRepository.createAccount(account);
    }

    public List<account> rankingAccount(int limit){
        return userRepository.rankingAccount(limit);
    }

    public int myRanking(Long id){
        return userRepository.myRanking(id);
    }

    public String readPassword(String name){
        return userRepository.readPassword(name);
    }

    public void updatePassword(String name,String password){
        userRepository.updatePassword(name,password);
    }

    public void deleteAccount(Long id){
        userRepository.deleteAccount(id);
    }

    public String readMail(String name){
        return userRepository.readMail(name);
    }

    
}
