package com.example.umigatari.model;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class solved {
    private Long id;   
    private int accountId;   
    private LocalDateTime quizDay;    
    private boolean quiz1;
    private boolean quiz2;
    private boolean quiz3;
    private boolean quiz4;
    private boolean quiz5;
    private boolean s1;
    private boolean s2;
    private boolean s3;
    private boolean s4;
    private boolean s5;
}
