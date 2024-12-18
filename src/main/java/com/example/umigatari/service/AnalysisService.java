package com.example.umigatari.service;

import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.umigatari.model.analysis;
import com.example.umigatari.repository.AnalysisRepository;
import com.example.umigatari.repository.QuizRepository;

@Service
public class AnalysisService {
    @Autowired
    private AnalysisRepository analysisRepository;

    @Autowired
    private QuizRepository quizRepository;

    //セクションごとの時間の追加  
    public void addSectionTime(Long id,Long account,int addrivute,int type,long timestamp,long timestamp2){
        int currentType = quizRepository.getType(id);
        long time = timestamp-timestamp2;
        analysisRepository.updateTime(account,addrivute,type,currentType,time);
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
        staytime [0] =analysisRepository.getStayTime();
        for(int i=0;i<5;i++){
            staytime [i+1]=analysisRepository.getStayTimeAddrivute(i+1);
        }
        return staytime;
    }

    //属性ごとセクションごとの時間の取得
    public double[][] updateSectionTime(){
        double[] sectiontime = new double[6];
        for(int i=0;i <6;i++ ){
            int prevqr = i+1;
            int presqr = i+2;
            sectiontime[i] = analysisRepository.getSection(prevqr,presqr );
        }
        double [][] sectionaddrivutetime = new double[5][6];
        for (int i=0;i<5;i++){
            int addrivute = i+1;
            for(int q=0;q<6;q++){
                int prevqr = q+1;
                int presqr = q+2;
                sectionaddrivutetime [i][q]=analysisRepository.getSectionAddrivute(prevqr, presqr, addrivute);
            }
        }
        double[][] updatesectiontime = new double[6][6];
        System.arraycopy(sectiontime, 0, updatesectiontime[0], 0, 6);
        for (int i = 0; i < 5; i++) {
            System.arraycopy(sectionaddrivutetime[i], 0, updatesectiontime[i + 1], 0, 6);
        }

    return updatesectiontime;
    }

    //属性ごとの時間の割合
    public analysis raito(analysis analysis){
        double [] staytime = analysis.getStaytime();
        double dou = staytime [0];
        double dou2 = 0;
        for(int i=0;i<5;i++){
            dou2 = dou2+staytime[i+1];
        }
        double [] douarr= new double[6];
        douarr[0] = dou;
        for(int i=1;i<6;i++){
            douarr[i] = Math.round((staytime[i] / dou2) * 1000.0) / 1000.0 * 100;
        }
        analysis.setStaytime(douarr);
        double [][] sectionstaytime = analysis.getSectionstaytime();
        double [] douarr2 = new double[6];
        for(int i=0;i<6;i++){
            for(int q=1;q<6;q++){
                douarr2[i]=sectionstaytime[i][q];
            }
        }
        double [][] douarr3 = new double[6][6];
        for(int i=0;i<6;i++){
            douarr3[i][0]=sectionstaytime[i][0];
            for(int q=1;q<6;q++){
                douarr3[q][i]=Math.round((sectionstaytime[q][i]/douarr2[i])*1000.0)/1000.0*100;
            }
        }
        analysis.setSectionstaytime(douarr3);
        return analysis;
    }
}
