package com.example.WhatsAppServer.DTO;

import lombok.Data;

@Data
public class EmojiResponseReq {
    private Integer userId;
    private Integer chatId;
    private String emoji;
}
