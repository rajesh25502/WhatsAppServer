package com.example.WhatsAppServer.Service;

import com.example.WhatsAppServer.DTO.GroupChatRequest;
import com.example.WhatsAppServer.DTO.MessageType;
import com.example.WhatsAppServer.Entity.ChatRoom;
import com.example.WhatsAppServer.Entity.Message;
import com.example.WhatsAppServer.Entity.User;
import com.example.WhatsAppServer.Exception.ChatRoomException;
import com.example.WhatsAppServer.Exception.UserException;
import com.example.WhatsAppServer.Repository.ChatRoomRepo;
import com.example.WhatsAppServer.Repository.MessageRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ChatRoomServiceImpl implements ChatRoomService{

    @Autowired
    private ChatRoomRepo chatRoomRepo;

    @Autowired
    private MessageRepo messageRepo;

    @Autowired
    private UserService userService;

    @Override
    public ChatRoom createChat(User registeredUser, Integer userId) throws UserException {
        User user=userService.findUserByID(userId);
        if(user==null)
        {
            throw new UserException("User not found with "+userId);
        }
        ChatRoom isChatExists=chatRoomRepo.findChatByIds(user,registeredUser);
        if(isChatExists!=null)
        {
            return isChatExists;
        }
        ChatRoom chat=new ChatRoom();
        chat.setChatName(user.getName());
        chat.setCreatedBy(registeredUser);
        chat.setIsGroup(false);
        chat.getAdmins().add(registeredUser);
        chat.getUsers().add(user);
        chat.getUsers().add(registeredUser);

        return chatRoomRepo.save(chat);
    }

    @Override
    public ChatRoom findChatById(Integer chatId) throws ChatRoomException {
        Optional<ChatRoom> chat=chatRoomRepo.findById(chatId);
        if(chat.isPresent())
        {
            return chat.get();
        }
        throw new ChatRoomException("Chat not found with : "+chatId);
    }

    @Override
    public List<ChatRoom> findAllChatOfUser(Integer userId) throws UserException {
        User user=userService.findUserByID(userId);
        List<ChatRoom> chats=chatRoomRepo.findChatByUserId(userId);
        return chats;
    }

    @Override
    public ChatRoom createChatGroup(GroupChatRequest req, User reqUser) throws UserException {
        ChatRoom groupChat=new ChatRoom();
        groupChat.setIsGroup(true);
        groupChat.setChatName(req.getChatName());
        groupChat.setChatImage(req.getChatImage());
        groupChat.setCreatedBy(reqUser);
        groupChat.getUsers().add(reqUser);
        groupChat.getAdmins().add(reqUser);
        for(Integer userID:req.getUserIds())
        {
            User user=userService.findUserByID(userID);
            groupChat.getUsers().add(user);
        }

        Message message=new Message();
        message.setChat(groupChat);
        message.setUser(reqUser);
        message.setText(reqUser.getName()+" has created the group ");
        message.setMessageType(MessageType.CREATE);
        message.setTimeStamp(LocalDateTime.now());

        groupChat.getMessages().add(message);

        return chatRoomRepo.save(groupChat);
    }

    @Override
    public ChatRoom addAdminToChatGroup(Integer userId, Integer chatId,User reqUser)throws UserException,ChatRoomException
    {
        ChatRoom group=findChatById(chatId);
        User user=userService.findUserByID(userId);

        if(!group.getAdmins().contains(reqUser))
        {
            throw new ChatRoomException("You do not have admin access to add admin");
        }
        group.getAdmins().add(user);
        group.getUsers().add(user);

        Message message=new Message();
        message.setChat(group);
        message.setUser(reqUser);
        message.setText(reqUser.getName()+" has added "+user.getName()+ " as new admin");
        message.setMessageType(MessageType.NEW_ADMIN);
        message.setTimeStamp(LocalDateTime.now());

        group.getMessages().add(message);

        return chatRoomRepo.save(group);
    }
    @Override
    public ChatRoom addUserToGroup(Integer userId, Integer chatId,User reqUser) throws UserException, ChatRoomException {
        ChatRoom group=findChatById(chatId);
        User user=userService.findUserByID(userId);

        if(!group.getAdmins().contains(reqUser))
        {
            throw new ChatRoomException("You do not have admin access to add user");
        }
        group.getUsers().add(user);

        Message message=new Message();
        message.setChat(group);
        message.setUser(reqUser);
        message.setText(reqUser.getName()+" has added "+user.getName()+ " to the group");
        message.setMessageType(MessageType.JOIN);
        message.setTimeStamp(LocalDateTime.now());

        group.getMessages().add(message);

        return chatRoomRepo.save(group);
    }

    @Override
    public ChatRoom renameChatGroup(Integer chatId, String groupName, User reqUser) throws ChatRoomException, UserException {
        ChatRoom group=findChatById(chatId);
        if(!group.getAdmins().contains(reqUser))
        {
            throw new ChatRoomException("You do not have admin access to change group name");
        }
        if(!group.getIsGroup())
            throw new ChatRoomException("you can only change the group chat name");

        group.setChatName(groupName);

        Message message=new Message();
        message.setChat(group);
        message.setUser(reqUser);
        message.setText(reqUser.getName()+" changed the group name to "+groupName);
        message.setMessageType(MessageType.RENAME);
        message.setTimeStamp(LocalDateTime.now());
        messageRepo.save(message);

        group.getMessages().add(message);

        return chatRoomRepo.save(group);
    }

    @Override
    public ChatRoom removeFromGroup(Integer userId, Integer chatId, User reqUser) throws ChatRoomException, UserException {
        ChatRoom group=findChatById(chatId);
        User user=userService.findUserByID(userId);

        if(userId.equals(reqUser.getId()))
        {
            exitGroup(chatId,user);
        }
        if(!group.getAdmins().contains(reqUser))
        {
            throw new ChatRoomException("You do not have admin access to remove user");
        }
        if(!group.getUsers().contains(user))
        {
            throw new ChatRoomException("User is not member of the group");
        }
        group.getUsers().remove(user);

        Message message=new Message();
        message.setChat(group);
        message.setUser(reqUser);
        message.setText(reqUser.getName()+" has removed "+user.getName()+ " from group");
        message.setMessageType(MessageType.REMOVE);
        message.setTimeStamp(LocalDateTime.now());

        group.getMessages().add(message);

        return chatRoomRepo.save(group);
    }
    @Override
    public ChatRoom exitGroup(Integer chatId,User reqUser)throws ChatRoomException,UserException
    {
        ChatRoom group=findChatById(chatId);
        if(!group.getUsers().contains(reqUser))
        {
            throw new ChatRoomException("You do not have access to the group");
        }
        if(group.getAdmins().contains(reqUser) && group.getAdmins().size()==1)
        {
            throw new ChatRoomException("Add new Admin before Leaving group since you are the only admin");
        }
        group.getUsers().remove(reqUser);
        group.getAdmins().remove(reqUser);

        Message message=new Message();
        message.setChat(group);
        message.setUser(reqUser);
        message.setText(reqUser.getName()+" has left the group ");
        message.setMessageType(MessageType.LEAVE);
        message.setTimeStamp(LocalDateTime.now());

        group.getMessages().add(message);

        return chatRoomRepo.save(group);
    }
    @Override
    public void deleteChat(Integer chatId, User reqUser) throws ChatRoomException, UserException {
        ChatRoom chat=findChatById(chatId);
        if(!chat.getUsers().contains(reqUser))
        {
            throw new ChatRoomException("You do not have access to the group");
        }
        chatRoomRepo.deleteById(chatId);
    }
}
