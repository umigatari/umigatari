package com.example.umigatari.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
