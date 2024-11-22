package com.example.umigatari.model;

import jakarta.persistence.Id;
import lombok.Data;

@Data
public class quiz {
    @Id
    private Long id;
    private String question;
    private String right;
    private String otherone;
    private String othertwo;
    private int type;
    private boolean check;
    
}
