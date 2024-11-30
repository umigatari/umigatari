package com.example.umigatari;

//エラー処理用のクラス  
public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}
