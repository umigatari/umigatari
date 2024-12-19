package com.example.umigatari.repository;

import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;



@Repository
public class AnalysisRepository {
    @Autowired
    public JdbcTemplate jdbcTemplate;

    //セクション時間を追加
    public void updateTime(Long account,int addrivute, int type,int currentType,Long time){
        String sql = "INSERT INTO sectiontime (accountid , addrivute , prevqr ,  presqr ,  untiltime) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,account,addrivute,type,currentType,time);
    }

    //セクションごとの時間を取得(全体)
    public Double getSection(int prevqr,int presqr) {
        String sql =  "SELECT AVG(untiltime) FROM sectiontime WHERE presqr = ? AND prevqr = ?";
        Double avg = jdbcTemplate.queryForObject(sql, Double.class,presqr,prevqr);
        return avg != null ? avg : 0; 
    }

    //属性ごとセクションごとの時間を取得(属性ごと)
    public Double getSectionAddrivute(int prevqr,int presqr,int addrivute) {
        String sql = "SELECT AVG(untiltime) FROM sectiontime WHERE presqr = ? AND prevqr = ? AND addrivute = ?";
        Double avg = jdbcTemplate.queryForObject(sql, Double.class,presqr,prevqr,addrivute);
        return avg != null ? avg : 0; 
    }

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
        String sql = "SELECT AVG(EXTRACT(EPOCH FROM (exittime - entrytime))) FROM staytime WHERE exittime IS NOT NULL";
        Double stayTime = jdbcTemplate.queryForObject(sql, Double.class);
        return stayTime != null ? stayTime : 0.0;     
    }

    //属性ごと退館時間の平均
    public double getStayTimeAddrivute(int addrivute){
        String sql = "SELECT AVG(EXTRACT(EPOCH FROM (exittime - entrytime))) FROM staytime WHERE exittime IS NOT NULL AND addrivute = ?"; 
        Double stayTime =jdbcTemplate.queryForObject(sql, Double.class,addrivute);   
        return stayTime != null ? stayTime : 0.0;          
    }
}