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
        return userRepository.createAccount(account);
    }

    public List<account> rankingAccount(account account){
        return userRepository.rankingAccount(account);
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

    public mail readMail(String name){
        userRepository.readMail(name);
    }

    
}
