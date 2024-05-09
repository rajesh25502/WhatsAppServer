package com.example.WhatsAppServer.Config;

import com.example.WhatsAppServer.DTO.MessageType;
import com.example.WhatsAppServer.Entity.Message;
import com.example.WhatsAppServer.Entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.Objects;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketEventListener {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event)
    {
        StompHeaderAccessor headerAccessor=StompHeaderAccessor.wrap(event.getMessage());
        String user=(String) Objects.requireNonNull(headerAccessor.getSessionAttributes()).get("user");
        if(user!=null)
        {
            log.info("User Disconnected : {}",user);
            Message message=Message.builder().messageType(MessageType.LEAVE).build();
            messagingTemplate.convertAndSend("/group",message);
        }
    }
}
