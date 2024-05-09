package com.example.WhatsAppServer.Service;

import com.example.WhatsAppServer.DTO.EmojiResponseReq;
import com.example.WhatsAppServer.Entity.Message;
import com.example.WhatsAppServer.Entity.User;
import com.example.WhatsAppServer.Exception.ChatRoomException;
import com.example.WhatsAppServer.Exception.MessageException;
import com.example.WhatsAppServer.Exception.UserException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface MessageService {

    public Message sendMessage(Integer chatId,String text,MultipartFile file,User user) throws UserException, ChatRoomException, IOException;

    public List<Message> getChatMessages(Integer chatId, User reqUser) throws ChatRoomException, UserException;

    public Message findMessageById(Integer messageId)throws MessageException;

    public Message setEmojiResponse(EmojiResponseReq req,Integer messageId) throws MessageException, UserException, ChatRoomException;
    public void deleteMessage(Integer messageId,User reqUser) throws MessageException, UserException;
}
