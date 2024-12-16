package com.example.umigatari.model;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class staytime {
    private int accountid;
    private LocalDateTime entrytime;
    private LocalDateTime exittime;
    private int addrivute;
}
