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

    //セクションごとの時間の追加  
    public void addSectionTime(Long id,Long account,int addrivute,int type,long timestamp,long timestamp2){
        int currentType = quizRepository.getType(id);
        long time = timestamp-timestamp2;
        analysisRepository.updateTime(account,addrivute,type,currentType,time);
    }

    public double[] updateStayTime(){
        double[] staytime = new double[6];
        staytime [0] =analysisRepository.getStayTimeAddrivute();
        for(int i=0;i<5;i++){
            staytime [i+1]=analysisRepository.getStayTime();
        }
        return staytime;
    }
    //セクションごとの時間の取得
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
        for (int q = 0; q < 6; q++) {
            updatesectiontime[0][q] = sectiontime[q];
         }
        for (int i = 0; i < 5; i++) {
            for (int q = 0; q < 6; q++) {
                updatesectiontime[i + 1][q] = sectionaddrivutetime[i][q];
            }
        }

    return updatesectiontime;
    }
}
