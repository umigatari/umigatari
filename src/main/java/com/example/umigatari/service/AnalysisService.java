package com.example.umigatari.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.umigatari.model.analysis;
import com.example.umigatari.model.answered;
import com.example.umigatari.model.timeanalysis;
import com.example.umigatari.repository.AnalysisRepository;
import com.example.umigatari.repository.QuizRepository;

@Service
public class AnalysisService {
    @Autowired
    private AnalysisRepository analysisRepository;

    @Autowired
    private QuizRepository quizRepository;

    public void frequencyUp(int type){
        analysisRepository.frequencyUp(type);
    }

    public void registerTime(Long id,int type,long timestamp,long timestamp2){
        int currentType = quizRepository.getType(id);
        long time = timestamp-timestamp2;
        analysisRepository.updateTime(type,currentType,time);
    }

    public List<answered> getAnswered(){
        return analysisRepository.getAnsweredSortedByFrequency();
    }

    public List<analysis> getAnalysis(){
        List<analysis> analysisList = new ArrayList<>();
        for(int i = 1;i <10;i++){
            analysis analysis = new analysis();
            //１を選んだら１の次のタイプを取得ないなら0
            int curType = analysisRepository.getcurrenttypeByType(i);
            if(curType==0){
                analysis.setCurrenttype(0);
                analysis.setCurrenttypetime(0);
            }else{
                analysis.setCurrenttype(curType);
                double time1 = analysisRepository.getAverageTimeUntilNext(curType, i);
                analysis.setCurrenttypetime(time1);
            }
            int preType = analysisRepository.getByprevioustType(i);
            if(preType==0){
                analysis.setPrevioustype(0);
                analysis.setPrevioustypetime(0);
            }else{
                analysis.setPrevioustype(preType);
                double time2 = analysisRepository.getAverageTimeUntilNext(i, preType);
                analysis.setPrevioustypetime(time2);
            }
            analysisList.add(analysis);
        }
        return analysisList;
    }

    public int getAllAnswerd(){
        return analysisRepository.getAllAnswerd();
    }

    public List<timeanalysis> getDetails(){
        return analysisRepository.getDetails();
    }
}
