package com.example.umigatari.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.umigatari.NotFoundException;
import com.example.umigatari.model.quiz;
import com.example.umigatari.repository.QuizRepository;

@Service
public class QuizService {
    @Autowired
    private QuizRepository quizRepository;

    //IDをもとにクイズを表示
    public quiz selectOneById(Long id){
        return quizRepository.readQuiz(id);
    }

    //クイズを３問表示
    public List<quiz> randomThreeQuiz(int type){
        return quizRepository.randomThreeQuiz(type);
    }

    //作成
    public void insertQuiz(quiz quiz){
        quizRepository.insertQuiz(quiz);
    }

    //削除
    public void deleteQuiz(Long id){
        quizRepository.deleteQuiz(id);
    }

    //更新
    public void updateQuiz(quiz quiz){
        quizRepository.updateQuiz(quiz);
    }

    //一覧
    public List<quiz> listQuiz(){
        return quizRepository.listQuiz();
    }

    //チェックが必要なクイズを表示
    public List<quiz> checkListQuiz(){
        List<quiz> quizzes = quizRepository.checkListQuiz();
        if (quizzes.isEmpty()) {
            throw new NotFoundException("チェックが必要なクイズが見つかりませんでした。");
        }
        return quizzes;
    }
    
}
