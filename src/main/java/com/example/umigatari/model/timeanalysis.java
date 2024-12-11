package com.example.umigatari.model;

import lombok.Data;

@Data
public class timeanalysis {
    private Long id;
    private int previousType;
    private int currentType;
    private long timeUntilNext;
}
