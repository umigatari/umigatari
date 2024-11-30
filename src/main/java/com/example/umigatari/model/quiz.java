package com.example.umigatari.model;

import java.time.LocalDateTime;

import jakarta.persistence.Id;
import lombok.Data;

@Data
public class quiz {
    @Id
    private Long id;
    private String question;
    private String correct;
    private String other_one;
    private String other_two;
    private int type;
    private boolean confirmation;
    private LocalDateTime creationday;
    private LocalDateTime updatedate;

    
}
