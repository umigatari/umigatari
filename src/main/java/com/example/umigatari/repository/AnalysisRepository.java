package com.example.umigatari.repository;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;



@Repository
public class AnalysisRepository {
    @Autowired
    public JdbcTemplate jdbcTemplate;

    //セクション時間を追加
    public void updateTime(Long account,int addrivute, int type,int currentType,long time){
        String sql = "INSERT INTO sectiontime (accountid , addrivute , prevqr ,  presqr ,  untiltime) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,account,addrivute,type,currentType,time);
    }

    //セクションごとの時間を取得(全体)
    public Double getSection(int prevqr,int presqr) {
        String sql = "SELECT AVG(timeUntilNext) FROM sectiontime WHERE currentType = ? AND previousType = ?";
        Double avg = jdbcTemplate.queryForObject(sql, Double.class,prevqr,presqr);
        return avg != null ? avg : 0; 
    }

    //セクションごとの時間を取得(属性ごと)
    public Double getSectionAddrivute(int prevqr,int presqr,int addrivute) {
        String sql = "SELECT AVG(timeUntilNext) FROM sectiontime WHERE currentType = ? AND previousType = ? AND addrivute = ?";
        Double avg = jdbcTemplate.queryForObject(sql, Double.class,prevqr,presqr,addrivute);
        return avg != null ? avg : 0; 
    }

    //入館時間の記録
    public void enterTime(Long accountid,LocalDateTime entertime,int addrivute){
        String sql = "INSERT INTO staytime (accountid, entrytime,addrivute) VALUES (?, ?,?)";
        jdbcTemplate.update(sql,accountid, entertime,addrivute);
    }

    //退館時間の記録
    public void exitTime(Long accountid,LocalDateTime entertime,LocalDateTime exittime){
        String sql ="UPDATE staytime SET exittime = ? WHERE accountid = ? AND entrytime = ?";
        jdbcTemplate.update(sql, accountid, entertime);
    }

    //退館時間の平均（全体）
    public double getStayTime(){
        String sql = "SELECT AVG(EXTRACT(EPOCH FROM (exittime - entrytime))) FROM staytime WHERE exittime IS NOT NULL";
        return jdbcTemplate.queryForObject(sql, Double.class);             
    }

    //退館時間の平均（属性ごと）
    public double getStayTimeAddrivute(){
        String sql = "SELECT addrivute, AVG(EXTRACT(EPOCH FROM (exittime - entrytime))) FROM staytime WHERE exittime IS NOT NULL GROUP BY addrivute";
        return jdbcTemplate.queryForObject(sql, Double.class);             
    }
}