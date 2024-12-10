package com.example.umigatari.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class AnalysisRepository {
    @Autowired
    public JdbcTemplate jdbcTemplate;

    public void frequencyUp(int type){
        String sql ="UPDATE answered SET frequency = frequency + 1 WHERE id = ?";
        jdbcTemplate.update(sql,type);
    }

    public void updateTime(int type,int currentType,long time){
        String sql = "INSERT INTO account (previoustype , currenttype, timeuntilnext) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql,type,currentType,time);
    }
}
