package com.example.umigatari.repository;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.umigatari.model.staytime;



@Repository
public class AnalysisRepository {
    @Autowired
    public JdbcTemplate jdbcTemplate;

    //入館時間の記録
    public void enterTime(Long accountid,Timestamp entertime,int addrivute){
        String sql = "INSERT INTO staytime (accountid, entrytime,addrivute) VALUES (?, ?,?)";
        jdbcTemplate.update(sql,accountid, entertime,addrivute);
    }

    //退館時間の記録
    public void exitTime(Long accountid,Timestamp entertime,Timestamp exittime){
        String sql ="UPDATE staytime SET exittime = ? WHERE accountid = ? AND entrytime = ?";
        jdbcTemplate.update(sql,exittime, accountid, entertime);
    }

    //退館時間の平均（全体）
    public double getStayTime(){
        String sql = "SELECT AVG(EXTRACT(EPOCH FROM (exittime - entrytime))) FROM staytime WHERE exittime IS NOT NULL AND addrivute != 0";
        Double stayTime = jdbcTemplate.queryForObject(sql, Double.class);
        return stayTime != null ? stayTime : 0.0;     
    }

    //属性ごと退館時間の平均
    public double getStayTimeAddrivute(int addrivute){
        String sql = "SELECT AVG(EXTRACT(EPOCH FROM (exittime - entrytime))) FROM staytime WHERE exittime IS NOT NULL AND addrivute = ?"; 
        Double stayTime =jdbcTemplate.queryForObject(sql, Double.class,addrivute);   
        return stayTime != null ? stayTime : 0.0;          
    }

    public List<staytime> getStayTimeData() {
        String sql = "SELECT * FROM staytime";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(staytime.class));
    }

}