package com.example.WhatsAppServer.Exception;

import lombok.Data;

@Data
public class ApplicationError {
    private int code;
    private String message;
    private String details;

}
