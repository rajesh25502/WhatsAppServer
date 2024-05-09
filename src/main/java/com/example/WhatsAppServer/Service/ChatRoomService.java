package com.example.WhatsAppServer.Service;

import com.example.WhatsAppServer.DTO.GroupChatRequest;
import com.example.WhatsAppServer.Entity.ChatRoom;
import com.example.WhatsAppServer.Entity.User;
import com.example.WhatsAppServer.Exception.ChatRoomException;
import com.example.WhatsAppServer.Exception.UserException;

import java.util.List;

public interface ChatRoomService {

    public ChatRoom createChat(User registeredUser, Integer userId)throws UserException;

    public ChatRoom findChatById(Integer chatId)throws ChatRoomException;

    public List<ChatRoom> findAllChatOfUser(Integer userId)throws UserException;

    public ChatRoom createChatGroup(GroupChatRequest req, User reqUserId)throws UserException;

    public ChatRoom addAdminToChatGroup(Integer userId, Integer chatId,User reqUserId)throws UserException,ChatRoomException;

    public ChatRoom addUserToGroup(Integer userId,Integer chatId,User reqUserId) throws UserException,ChatRoomException;

    public ChatRoom renameChatGroup(Integer chatId,String groupName,User reqUserId)throws ChatRoomException,UserException;

    public ChatRoom removeFromGroup(Integer userId,Integer chatId,User reqUserId) throws ChatRoomException,UserException;

    public ChatRoom exitGroup(Integer chatId,User reqUserId)throws ChatRoomException,UserException;

    public void deleteChat(Integer chatId,User reqUser)throws ChatRoomException,UserException;
}
