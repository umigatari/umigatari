package com.example.umigatari.service;

import java.io.IOException;
import java.io.Writer;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.umigatari.model.staytime;
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
            double stime = analysisRepository.getStayTimeAddrivute(i+1);
            BigDecimal bd = new BigDecimal(stime/60);
            bd = bd.setScale(0, RoundingMode.HALF_UP);
            staytime[i] = bd.doubleValue();
        }

        double stime =analysisRepository.getStayTime();
        BigDecimal bd = new BigDecimal(stime/60);
            bd = bd.setScale(0, RoundingMode.HALF_UP);
            staytime[5] = bd.doubleValue();

        return staytime;
    }

    @SuppressWarnings("CallToPrintStackTrace")
        public void exportStayTimeToCsv( Writer writer) throws IOException {
        List<staytime> stayTimes = analysisRepository.getStayTimeData();
        // CSVのヘッダーを書き込み
        writer.write("アカウントID, 入館時間, 退館時間, 属性\n");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        // ユーザーのデータをCSV形式で書き込み
        for (staytime stayTime : stayTimes) {
            try {
                String entryTime = stayTime.getEntrytime() != null ? sdf.format(stayTime.getEntrytime()) : "";
                String exitTime = stayTime.getExittime() != null ? sdf.format(stayTime.getExittime()) : "";
        
                writer.write(stayTime.getAccountid() + ",");
                writer.write(entryTime + ",");
                writer.write(exitTime + ",");
                writer.write(stayTime.getAddrivute()  + "\n");
            } catch (IOException e) {
                System.err.println("Error processing stayTime record: " + stayTime);
                e.printStackTrace();
            }
        }
        writer.flush();
        
    }


}
