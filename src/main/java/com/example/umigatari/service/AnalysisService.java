package com.example.umigatari.service;

import java.sql.Timestamp;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.umigatari.model.analysis;
import com.example.umigatari.repository.AnalysisRepository;

@Service
public class AnalysisService {
    @Autowired
    private AnalysisRepository analysisRepository;

    //セクションごとの時間の追加  
    public void addSectionTime(Long id,Long account,int addrivute,int prevtype,int prestype,long timestamp,long timestamp2){
        long time = timestamp-timestamp2;
        analysisRepository.updateTime(account,addrivute,prevtype,prestype,time);
    }

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

    //属性ごとセクションごとの時間の取得
    public Double[][] updateSectionTime(){
        Double [][] sectionaddrivutetime = new Double[6][6];
        for (int i=0;i<5;i++){
            int addrivute = i+1;
            for(int q=0;q<6;q++){
                int prevqr = q;
                int presqr = q+1;
                sectionaddrivutetime [i][q]=analysisRepository.getSectionAddrivute(prevqr, presqr, addrivute);
            }
        }
        for(int i=0;i <6;i++ ){
            int prevqr = i;
            int presqr = i+1;
            sectionaddrivutetime[5][i] = analysisRepository.getSection(prevqr,presqr );
        }
    return sectionaddrivutetime;
    }

    //属性ごとの時間の割合
    public analysis raito(analysis analysis){
        //staytime
        double [] staytime = analysis.getStaytime();
        //System.out.print(staytime[5]);
        double [] douarr= new double[6];
        for(int i=0;i<6;i++){
            douarr[i] = Math.round(staytime[i]*100/60)/100;
        }
        analysis.setStaytime(douarr);
        // sectionstaytime
        Double[][] sectionstaytime = analysis.getSectionstaytime();
        Double[] douarr2 = new Double[6];
        Arrays.fill(douarr2, 0.0);
        for (int i = 0; i < 6; i++) {
                for (int q = 0; q < 6; q++) {
                    douarr2[i] = sectionstaytime[i][q]+douarr2[i];  
                    System.out.print(douarr2[i]);
                    }
            }
            Double[][] douarr3 = new Double[6][6];
        for (int i = 0; i < 6; i++) {
                for (int q = 0; q < 6; q++) {
                    douarr3[i][q] = (double)Math.round((sectionstaytime[i][q] / douarr2[i]) * 100);
                    }
            }
            analysis.setSectionstaytime(douarr3);
        return analysis;
    }
}
