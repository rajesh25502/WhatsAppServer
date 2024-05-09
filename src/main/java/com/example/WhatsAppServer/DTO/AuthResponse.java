package com.example.WhatsAppServer.DTO;

import lombok.Data;

@Data
public class AuthResponse {

    private  String jwt;
    private String message;
    private boolean isAuth;
}
