package com.example.umigatari.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.umigatari.model.quiz;

@Repository
public class QuizRepository {
    
    @Autowired
    public JdbcTemplate jdbcTemplate;
    
    //ランダムにクイズを3つ表示する
    public List<quiz> randomThreeQuiz(int type) {
        String sql = "SELECT * FROM quiz WHERE type = ? ORDER BY RANDOM() LIMIT 3";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(quiz.class),type);
    }

    //クイズを参照する
    public quiz readQuiz(Long id) {
        String sql = "SELECT * FROM quiz WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(quiz.class), id);
    }


    //追加する
    public void insertQuiz(quiz quiz){
        String sql = "insert into quiz (question,correct,other_one,other_two,type,confirmation) values (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,quiz.getQuestion(),quiz.getCorrect(),quiz.getOther_one(),quiz.getOther_two(),quiz.getType(),quiz.isConfirmation());
    }

    //削除する
    public void deleteQuiz(Long id){
        String sql = "DELETE FROM quiz WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    //更新する
    public void updateQuiz(quiz quiz){
        String sql = "UPDATE quiz SET question = ?, correct = ?, other_one = ?, other_two = ?, type = ?, confirmation = ?, updatedate = NOW() WHERE id = ?";
        jdbcTemplate.update(sql, quiz.getQuestion(), quiz.getCorrect(), quiz.getOther_one(), quiz.getOther_two(),quiz.getType(),quiz.isConfirmation(),quiz.getId());
    }

    //一覧を表示する
    public List<quiz> listQuiz(){
    String sql = "SELECT * FROM quiz";
    return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(quiz.class));
    }

    //一覧を表示するtype
    public List<quiz> readTypeQuiz(int type){
        String sql = "SELECT * FROM quiz where type = ?";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(quiz.class),type);
    }

    //問題チェック一覧を表示するtype
    public List<quiz> readTypeQuizCheck(int type){
        String sql = "SELECT * FROM quiz where type = ? AND confirmation = true";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(quiz.class),type);
    }

    //一覧を表示するASC
    public List<quiz> readOrderAscQuiz(){
        String sql = "SELECT * FROM quiz order by creationday ASC";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(quiz.class));
    }

    //問題チェック一覧を表示するASC 
    public List<quiz> readOrderAscQuizCheck(){
        String sql = "SELECT * FROM quiz WHERE confirmation = true ORDER BY creationday ASC";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(quiz.class));
    }
    
    //一覧を表示するdesc
    public List<quiz> readOrderDescQuiz(){
        String sql = "SELECT * FROM quiz order by creationday Desc";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(quiz.class));
    }

    //問題チェック一覧を表示するdesc
    public List<quiz> readOrderDescQuizCheck(){
        String sql = "SELECT * FROM quiz WHERE confirmation = true order by creationday Desc";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(quiz.class));
    }

    //一覧を表示するdesc type order
    public List<quiz> readDesc(int type){
        String sql = "SELECT * FROM quiz WHERE type = ? ORDER BY creationday DESC;";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(quiz.class),type);
    }

    //問題チェック一覧を表示するdesc type order
    public List<quiz> readDescCheck(int type){
        String sql = "SELECT * FROM quiz WHERE type = ? AND confirmation = true ORDER BY creationday DESC;";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(quiz.class),type);
    }

    //一覧を表示するasc type order
    public List<quiz> readAsc(int type){
        String sql = "SELECT * FROM quiz WHERE type = ? ORDER BY creationday ASC";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(quiz.class),type);
    }

    //問題チェック一覧を表示するasc type order
    public List<quiz> readAscCheck(int type){
        String sql = "SELECT * FROM quiz WHERE type = ? AND confirmation = true ORDER BY creationday ASC";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(quiz.class),type);
    }

    //チェックが必要な一覧を表示する
    public List<quiz> checkListQuiz() {
        String sql = "SELECT * FROM quiz WHERE  confirmation = true";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(quiz.class));
    }

    //チェック済みにする
    public void updateConfirmation(Long id){
        String sql ="UPDATE quiz SET confirmation = false WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    //チェックを解除する
    public void unpostConfirmation(Long id){
        String sql ="UPDATE quiz SET confirmation = true WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    //通知の件数を表示
    public int getNotice() {
        String sql = "SELECT COUNT(*) FROM quiz WHERE confirmation = TRUE";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class);
        return count != null ? count : 0; // null の場合は 0 を返す
    }

    //IDをもとにtypeを表示
    public int getType(Long id){
        String sql ="select type from quiz where id = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class,id);
    }

}
