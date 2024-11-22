package com.example.umigatari.model;

import jakarta.persistence.Id;
import lombok.Data;

@Data
public class account {
    @Id
    private Long id;
    private String name;
    private String password;
    private int count;
    private String mail;
}
