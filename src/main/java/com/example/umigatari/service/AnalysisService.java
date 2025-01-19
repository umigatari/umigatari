package com.example.umigatari.service;

import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.umigatari.repository.AnalysisRepository;

@Service
public class AnalysisService {
    @Autowired
    private AnalysisRepository analysisRepository;

    //入館時間の記録
    public Timestamp enterTime(Long accountid,int addrivute,Timestamp entertime){
        analysisRepository.enterTime(accountid, entertime, addrivute);
        return entertime;
    }

    //退館時間の記録
    public void exitTime(Long accountid,Timestamp entertime ){
        long currentMillis = System.currentTimeMillis();
        Timestamp exittime = new Timestamp(currentMillis);
        analysisRepository.exitTime(accountid, entertime,exittime);
    }

    //属性ごとの滞在時間の取得
    public double[] updateStayTime(){
        double[] staytime = new double[6];
        for(int i=0;i<5;i++){
            staytime [i]=analysisRepository.getStayTimeAddrivute(i+1);
        }
        staytime [5] =analysisRepository.getStayTime();
        //System.out.print(analysisRepository.getStayTime());
        return staytime;
    }



}
