package com.example.umigatari.model;

import jakarta.persistence.Id;
import lombok.Data;

@Data
public class answered {
        @Id
    private int id;
    private int frequency;
}
