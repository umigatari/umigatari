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

    //type
    public List<quiz> selectByType(int type){
        return quizRepository.readTypeQuiz(type);
    }

    //type check
    public List<quiz> selectByTypeCheck(int type){
        return quizRepository.readTypeQuizCheck(type);
    }

    //order
    public List<quiz> selectByOrder(String order){
        //System.out.println(order);
        if("asc".equals(order)){
            List<quiz> quiz =quizRepository.readOrderAscQuiz();
            return quiz;
        }else if("desc".equals(order)){
        return quizRepository.readOrderDescQuiz();}
        List<quiz> quiz =null;
        return quiz;
        
    }

    //order check
    public List<quiz> selectByOrderCheck(String order){
        //System.out.println(order);
        if("asc".equals(order)){
            List<quiz> quiz =quizRepository.readOrderAscQuizCheck();        
            return quiz;
        }else if("desc".equals(order)){
        return quizRepository.readOrderDescQuizCheck();}
        List<quiz> quiz =null;
        return quiz;
        
    }

    //order type
    public List<quiz> readOrderTypeQuiz(String order,int type){
        if("asc".equals(order)){
            return quizRepository.readAsc(type);
        }else if("desc".equals(order)){
        return quizRepository.readDesc(type);}
        List<quiz> quiz =quizRepository.listQuiz();
        return quiz;
    }

    //order type check
    public List<quiz> readOrderTypeQuizCheck(String order,int type){
        if("asc".equals(order)){
            return quizRepository.readAscCheck(type);
        }else if("desc".equals(order)){
        return quizRepository.readDescCheck(type);}
        List<quiz> quiz =quizRepository.listQuiz();
        return quiz;
    }

    //クイズを３問表示
    public List<quiz> randomThreeQuiz(){
        return quizRepository.randomThreeQuiz();
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

    //問題チェック済みにする
    public void updateConfirmation(Long id){
        quizRepository.updateConfirmation(id);
    }

    //問題チェックを解除する
    public void unpostConfirmation(Long id){
        quizRepository.unpostConfirmation(id);
    }

    //通知の件数を表示
    public int getNotice(){
       return quizRepository.getNotice();
    }
    
}
