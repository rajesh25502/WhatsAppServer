package com.example.WhatsAppServer.Service;

import com.example.WhatsAppServer.DTO.Emoji;
import com.example.WhatsAppServer.DTO.EmojiResponseReq;
import com.example.WhatsAppServer.DTO.MessageType;
import com.example.WhatsAppServer.Entity.ChatRoom;
import com.example.WhatsAppServer.Entity.FileMetaData;
import com.example.WhatsAppServer.Entity.Message;
import com.example.WhatsAppServer.Entity.User;
import com.example.WhatsAppServer.Exception.ChatRoomException;
import com.example.WhatsAppServer.Exception.MessageException;
import com.example.WhatsAppServer.Exception.UserException;
import com.example.WhatsAppServer.Repository.FileMetaDataRepo;
import com.example.WhatsAppServer.Repository.MessageRepo;
import jakarta.servlet.ServletContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class MessageServiceImpl implements MessageService{
    @Autowired
    private MessageRepo messageRepo;

    @Autowired
    private ServletContext servletContext;
    @Autowired
    private FileMetaDataRepo fileMetaDataRepo;
    @Autowired
    private UserService userService;

    @Autowired
    private  ChatRoomService chatRoomService;
    @Override
    public Message sendMessage(Integer chatId,String text,MultipartFile file,User user) throws UserException, ChatRoomException, IOException {

        ChatRoom chat=chatRoomService.findChatById(chatId);
        if(!chat.getUsers().contains(user))
            throw new UserException("you are not member of this chat");
        Message message=new Message();
        message.setChat(chat);
        message.setUser(user);
        message.setMessageType(MessageType.SEND);
        if(text==null && file==null)
            return new Message();
        if(text!=null && !text.isEmpty())
            message.setText(text);
        if(file!=null && !file.isEmpty())
        {
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

            String name = file.getOriginalFilename();
            String mimeType = servletContext.getMimeType(name);
            if (mimeType.startsWith("image/")) {
                Path picturePath = Paths.get("root/picture/"+name);
                Files.copy(file.getInputStream(),picturePath);
            } else {
                Path videoPath = Paths.get("root/video/"+name);
                Files.copy(file.getInputStream(),videoPath);
            }

            FileMetaData metadata = new FileMetaData();
            metadata.setFileName(file.getOriginalFilename());
            metadata.setFileType(file.getContentType());
            metadata.setFileSize(file.getSize());

            fileMetaDataRepo.save(metadata);

            message.setFileMetaData(metadata);
        }

        message.setTimeStamp(LocalDateTime.now());
        return messageRepo.save(message);
    }

    @Override
    public List<Message> getChatMessages(Integer chatId,User reqUser) throws ChatRoomException, UserException {
        ChatRoom chat=chatRoomService.findChatById(chatId);

        if(!chat.getUsers().contains(reqUser))
        {
            throw new UserException("You do not have access to this chat");
        }
        List<Message> messages=messageRepo.findByChatId(chatId);
        return messages;
    }

    @Override
    public Message findMessageById(Integer messageId) throws MessageException {
        Optional<Message> message=messageRepo.findById(messageId);
        if(!message.isPresent())
        {
            throw new MessageException("Message not found with the id"+messageId);
        }
        return message.get();
    }

    @Override
    public Message setEmojiResponse(EmojiResponseReq req,Integer messageId) throws MessageException, UserException, ChatRoomException {
        User user=userService.findUserByID(req.getUserId());
        ChatRoom chat=chatRoomService.findChatById(req.getChatId());
        if(!chat.getUsers().contains(user))
            throw new UserException("you are not member of this chat");

        Message message=findMessageById(messageId);
        if(!chat.getMessages().contains(message))
            throw new MessageException("This chat does not contain message with id "+messageId);
        message.getEmoji().add(Emoji.valueOf(req.getEmoji()));
        return messageRepo.save(message);
    }
    @Override
    public void deleteMessage(Integer messageId,User reqUser) throws MessageException, UserException {
        Message message=findMessageById(messageId);
        if(!message.getUser().getId().equals(reqUser.getId()))
        {
            throw new UserException("Cannot delete other user messages");
        }
        messageRepo.deleteById(messageId);
    }
}
