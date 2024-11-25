package com.example.umigatari.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class QuizRepository {
    
    @Autowired
    public JdbcTemplate jdbcTemplate;

    public quiz readQuiz(){

    }

    public quiz randomThreeQuiz(){

    }

    public void insertQuiz(){

    }

    public void deleteQuiz(){

    }

    public void updateQuiz(){

    }

    public quiz listQuiz(){

    }

    public quiz checkListQuiz(){
        
    }
}
