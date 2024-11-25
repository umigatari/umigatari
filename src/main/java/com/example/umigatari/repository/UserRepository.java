package com.example.umigatari.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {
    
    @Autowired
    public JdbcTemplate jdbcTemplate;

    public void createAccount(){

    }

    public account rankingAccount(){

    }

    public rank myRanking(){

    }

    public password readPassword(){

    }

    public void updatePassword(){

    }

    public void deleteAccount(){

    }

    public mail readMail(){
        
    }
}
