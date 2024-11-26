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
    public List<quiz> randomThreeQuiz() {
        String sql = "SELECT * FROM quiz ORDER BY RANDOM() LIMIT 3";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(quiz.class));
    }

    //クイズを参照する
    public quiz readQuiz(Long id) {
        String sql = "SELECT * FROM quiz WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(quiz.class), id);
}

    //追加する
    public void insertQuiz(quiz quiz){
        String sql = "insert into quiz (question,correct,other_one,other_two,type,confirmation) values (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,quiz.getQuestion(),quiz.getCorrect(),quiz.getOtherone(),quiz.getOthertwo(),quiz.getType(),quiz.isConfirmation());
    }

    //削除する
    public void deleteQuiz(Long id){
        String sql = "DELETE FROM quiz WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    //更新する
    public void updateQuiz(quiz quiz){
        String sql = "UPDATE quizzes SET question = ?, correct = ?, otherone = ?,othertwo = ?,type = ?,confirmation = ? WHERE id = ?";
        jdbcTemplate.update(sql, quiz.getQuestion(), quiz.getCorrect(), quiz.getOtherone(), quiz.getOthertwo(),quiz.getType(),quiz.isConfirmation(),quiz.getId());
    }

    //一覧を表示する
    public List<quiz> listQuiz(){
    String sql = "SELECT * FROM quiz";
    return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(quiz.class));
    }

    //チェックが必要な一覧を表示する
    public List<quiz> checkListQuiz() {
        String sql = "SELECT * FROM quiz WHERE  confirmation = TRUE";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(quiz.class));
    }
}
