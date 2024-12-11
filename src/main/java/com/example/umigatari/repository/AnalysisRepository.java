package com.example.umigatari.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.umigatari.model.answered;
import com.example.umigatari.model.timeanalysis;

@Repository
public class AnalysisRepository {
    @Autowired
    public JdbcTemplate jdbcTemplate;

    public void frequencyUp(int type){
        String sql ="UPDATE answered SET frequency = frequency + 1 WHERE id = ?";
        jdbcTemplate.update(sql,type);
    }

    public void updateTime(int type,int currentType,long time){
        String sql = "INSERT INTO timeanalysis (previoustype ,  currenttype ,  timeuntilnext) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql,type,currentType,time);
    }

    public List<answered> getAnsweredSortedByFrequency() {
        String sql = "SELECT id, frequency FROM answered ORDER BY frequency DESC"; 
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(answered.class));
    }

    //指定したタイプの次のタイプを取得
    public Integer getcurrenttypeByType(int type) {
    String sql = "SELECT currenttype FROM timeanalysis WHERE previousType = ? GROUP BY currenttype ORDER BY COUNT(*) DESC LIMIT 1";
        try {
            return jdbcTemplate.queryForObject(sql, Integer.class, type);
        } catch (EmptyResultDataAccessException e) {
            return 0; // デフォルト値を返す
        }
    }

    //指定したタイプの前のタイプを取得
    public Integer getByprevioustType(int type) {
    String sql = "SELECT previousType FROM timeanalysis WHERE currenttype = ? GROUP BY previousType ORDER BY COUNT(*) DESC LIMIT 1";
        try {
            return jdbcTemplate.queryForObject(sql, Integer.class, type);
        } catch (EmptyResultDataAccessException e) {
            return 0; // デフォルト値を返す
        }
    }

    public Double getAverageTimeUntilNext(int currentType,int previousType) {
        String sql = "SELECT AVG(timeUntilNext) FROM timeanalysis WHERE currentType = ? AND previousType = ?";
        Double avg = jdbcTemplate.queryForObject(sql, Double.class,currentType,previousType);
        return avg != null ? avg : 0; 
    }

    public int getAllAnswerd(){
        String sql = "SELECT SUM(frequency) AS total_frequency FROM answered";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

    public List<timeanalysis> getDetails(){
        String sql = "select * from timeanalysis";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(timeanalysis.class));
    }
}