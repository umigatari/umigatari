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

    public List<quiz> randomThreeQuiz() {
        String sql = "SELECT * FROM quiz ORDER BY RANDOM() LIMIT 3";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(quiz.class));
    }

    public quiz readQuiz(Long id) {
        String sql = "SELECT * FROM quiz WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(quiz.class), id);
}


    public void insertQuiz(quiz quiz){
        String sql = "insert into quiz (question,correct,other_one,other_two,type,confirmation) values (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,quiz.getQuestion(),quiz.getCorrect(),quiz.getOtherone(),quiz.getOthertwo(),quiz.getType(),quiz.isConfirmation());
    }

    public void deleteQuiz(Long id){
        String sql = "DELETE FROM quiz WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public void updateQuiz(quiz quiz){
        String sql = "UPDATE quizzes SET question = ?, correct = ?, otherone = ?,othertwo = ?,type = ?,confirmation = ? WHERE id = ?";
        jdbcTemplate.update(sql, quiz.getQuestion(), quiz.getCorrect(), quiz.getOtherone(), quiz.getOthertwo(),quiz.getType(),quiz.isConfirmation(),quiz.getId());
    }

    public List<quiz> listQuiz(){
    String sql = "SELECT * FROM quiz";
    return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(quiz.class));
    }

    public List<quiz> checkListQuiz() {
        String sql = "SELECT * FROM quiz WHERE  confirmation = TRUE";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(quiz.class));
    }
}
