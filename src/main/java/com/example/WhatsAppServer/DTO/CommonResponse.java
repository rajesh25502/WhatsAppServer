package com.example.WhatsAppServer.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.security.PrivateKey;

@Data
@AllArgsConstructor
public class CommonResponse {

    private String message;
    private boolean status;
}
