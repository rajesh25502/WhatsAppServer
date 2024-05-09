package com.example.WhatsAppServer.Controller;

import com.example.WhatsAppServer.Entity.FileMetaData;
import com.example.WhatsAppServer.Entity.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.Objects;

@Controller
public class RealTimeChat {
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/message")
    @SendTo("/group/public")
    public Message recieveMessage(@Payload Message message, SimpMessageHeaderAccessor headerAccessor)
    {
        Objects.requireNonNull(headerAccessor.getSessionAttributes()).put("user",message.getUser().getName());
        simpMessagingTemplate.convertAndSend("/group"+message.getChat().getId().toString(),message);
        return message;
    }

}
