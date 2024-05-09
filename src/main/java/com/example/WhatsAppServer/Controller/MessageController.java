package com.example.WhatsAppServer.Controller;

import com.example.WhatsAppServer.DTO.CommonResponse;
import com.example.WhatsAppServer.DTO.EmojiResponseReq;
import com.example.WhatsAppServer.Entity.Message;
import com.example.WhatsAppServer.Entity.User;
import com.example.WhatsAppServer.Exception.ChatRoomException;
import com.example.WhatsAppServer.Exception.MessageException;
import com.example.WhatsAppServer.Exception.UserException;
import com.example.WhatsAppServer.Service.MessageService;
import com.example.WhatsAppServer.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    @Autowired
    private UserService userService;

    @Autowired
    private MessageService messageService;

    @PostMapping("/{chatId}/send")
    public ResponseEntity<Message> sendMessage(@PathVariable Integer chatId,
                                               @RequestParam(value = "text",required = false) String text,
                                               @RequestParam(value = "file",required = false) MultipartFile file,
                                               @RequestHeader("Authorization") String jwt) throws UserException, ChatRoomException, IOException {
        User user=userService.findUserProfile(jwt);
        Message message=messageService.sendMessage(chatId,text,file,user);
        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }
    @GetMapping("/{chatId}/chat")
    public ResponseEntity<List<Message>> getChatMessages(@PathVariable Integer chatId,
                                               @RequestHeader("Authorization") String jwt)throws UserException, ChatRoomException
    {
        User user=userService.findUserProfile(jwt);
        List<Message> messages=messageService.getChatMessages(chatId,user);
        return new ResponseEntity<>(messages, HttpStatus.OK);
    }
    @PutMapping("/{messageId}/emoji")
    public ResponseEntity<Message> setEmojiResponse(@RequestBody EmojiResponseReq req,
                                                    @PathVariable Integer messageId,
                                                    @RequestHeader("Authorization") String jwt) throws MessageException, ChatRoomException, UserException {
        User user=userService.findUserProfile(jwt);
        Message message=messageService.setEmojiResponse(req,messageId);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
    @GetMapping("/{messageId}")
    public ResponseEntity<Message> getMessageById(@PathVariable Integer messageId,
                                                     @RequestHeader("Authorization") String jwt) throws UserException, MessageException {
        User user=userService.findUserProfile(jwt);
        Message message=messageService.findMessageById(messageId);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @DeleteMapping("/{messageId}/delete")
    public ResponseEntity<CommonResponse> deleteMessageById(@PathVariable Integer messageId,
                                                            @RequestHeader("Authorization") String jwt) throws UserException, MessageException {
        User user=userService.findUserProfile(jwt);
        messageService.deleteMessage(messageId,user);
        CommonResponse resp=new CommonResponse("Message deleted successfully",true);
        return new ResponseEntity<>(resp, HttpStatus.OK);
    }
}
