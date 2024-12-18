package com.example.umigatari.model;
import java.sql.Timestamp;

import lombok.Data;


@Data
public class staytime {
    private int accountid;
    private Timestamp entrytime;
    private Timestamp exittime;
    private int addrivute;
}
