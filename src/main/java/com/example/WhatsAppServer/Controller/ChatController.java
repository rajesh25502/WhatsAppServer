package com.example.WhatsAppServer.Controller;

import com.example.WhatsAppServer.DTO.CommonResponse;
import com.example.WhatsAppServer.DTO.GroupChatRequest;
import com.example.WhatsAppServer.DTO.SingleChatReq;
import com.example.WhatsAppServer.Entity.ChatRoom;
import com.example.WhatsAppServer.Entity.User;
import com.example.WhatsAppServer.Exception.ChatRoomException;
import com.example.WhatsAppServer.Exception.UserException;
import com.example.WhatsAppServer.Service.ChatRoomService;
import com.example.WhatsAppServer.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpResponse;
import java.util.List;

@RestController
@RequestMapping("/api/chats")
public class ChatController {

    @Autowired
    private ChatRoomService chatRoomService;

    @Autowired
    private UserService userService;

    @PostMapping("/single")
    public ResponseEntity<ChatRoom> createChatRoom(@RequestBody SingleChatReq singleChatReq,
                                                   @RequestHeader("Authorization") String jwt)throws UserException
    {
        User user=userService.findUserProfile(jwt);
        ChatRoom chat=chatRoomService.createChat(user,singleChatReq.getUserId());
        return new ResponseEntity<>(chat, HttpStatus.CREATED);
    }

    @PostMapping("/group")
    public ResponseEntity<ChatRoom> createChatGroup(@RequestBody GroupChatRequest groupChatReq,
                                                   @RequestHeader("Authorization") String jwt)throws UserException
    {
        User user=userService.findUserProfile(jwt);
        ChatRoom chat=chatRoomService.createChatGroup(groupChatReq,user);
        return new ResponseEntity<>(chat, HttpStatus.CREATED);
    }

    @GetMapping("/{chatId}")
    public ResponseEntity<ChatRoom> findChat(@PathVariable Integer chatId,
                                                    @RequestHeader("Authorization") String jwt) throws UserException, ChatRoomException {
        User user=userService.findUserProfile(jwt);
        ChatRoom chat=chatRoomService.findChatById(chatId);
        return new ResponseEntity<>(chat, HttpStatus.OK);
    }

    @GetMapping("/allChat")
    public ResponseEntity<List<ChatRoom>> FindAllChatsOfUser(@RequestHeader("Authorization") String jwt) throws UserException, ChatRoomException {
        User user=userService.findUserProfile(jwt);
        List<ChatRoom> chats=chatRoomService.findAllChatOfUser(user.getId());
        return new ResponseEntity<>(chats, HttpStatus.OK);
    }

    @PutMapping("/{chatId}/addUser/{userId}")
    public ResponseEntity<ChatRoom> addUserToGroup(@PathVariable Integer chatId,
                                                             @PathVariable Integer userId,
                                                             @RequestHeader("Authorization") String jwt) throws UserException, ChatRoomException {
        User user=userService.findUserProfile(jwt);
        ChatRoom chat=chatRoomService.addUserToGroup(userId,chatId,user);
        return new ResponseEntity<>(chat, HttpStatus.OK);
    }

    @PutMapping("/{chatId}/addAdmin/{userId}")
    public ResponseEntity<ChatRoom> addAdminToGroup(@PathVariable Integer chatId,
                                                   @PathVariable Integer userId,
                                                   @RequestHeader("Authorization") String jwt) throws UserException, ChatRoomException {
        User user=userService.findUserProfile(jwt);
        ChatRoom chat=chatRoomService.addAdminToChatGroup(userId,chatId,user);
        return new ResponseEntity<>(chat, HttpStatus.OK);
    }


    @PutMapping("/{chatId}/renameGroup/{newName}")
    public ResponseEntity<ChatRoom> renameGroup(@PathVariable Integer chatId,
                                                    @PathVariable String newName,
                                                    @RequestHeader("Authorization") String jwt) throws UserException, ChatRoomException {
        User user=userService.findUserProfile(jwt);
        ChatRoom chat=chatRoomService.renameChatGroup(chatId,newName,user);
        return new ResponseEntity<>(chat, HttpStatus.OK);
    }

    @PutMapping("/{chatId}/removeUser/{userId}")
    public ResponseEntity<ChatRoom> removeFromGroup(@PathVariable Integer chatId,
                                                @PathVariable Integer userId,
                                                @RequestHeader("Authorization") String jwt) throws UserException, ChatRoomException {
        User user=userService.findUserProfile(jwt);
        ChatRoom chat=chatRoomService.removeFromGroup(userId,chatId,user);
        return new ResponseEntity<>(chat, HttpStatus.OK);
    }

    @PutMapping("/{chatId}/exit")
    public ResponseEntity<ChatRoom> exitGroup(@PathVariable Integer chatId,
                                                    @RequestHeader("Authorization") String jwt) throws UserException, ChatRoomException {
        User user=userService.findUserProfile(jwt);
        ChatRoom chat=chatRoomService.exitGroup(chatId,user);
        return new ResponseEntity<>(chat, HttpStatus.OK);
    }

    @DeleteMapping("/{chatId}/delete")
    public ResponseEntity<CommonResponse> deleteChat(@PathVariable Integer chatId,
                                                     @RequestHeader("Authorization") String jwt) throws UserException, ChatRoomException {
        User user=userService.findUserProfile(jwt);
        chatRoomService.deleteChat(chatId,user);
        CommonResponse resp=new CommonResponse("Chat deleted successfully",true);
        return new ResponseEntity<>(resp, HttpStatus.OK);
    }
}
