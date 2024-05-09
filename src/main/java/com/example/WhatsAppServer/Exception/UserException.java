package com.example.WhatsAppServer.Exception;

import com.example.WhatsAppServer.Entity.User;

public class UserException extends Exception {
    public UserException(String message)
    {
        super(message);
    }
}
