package com.example.WhatsAppServer.Controller;

import com.example.WhatsAppServer.DTO.UpdateUserRequest;
import com.example.WhatsAppServer.Entity.User;
import com.example.WhatsAppServer.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<User> getUserProfileByToken(@RequestHeader("Authorization") String jwt) throws Exception {
        User user=userService.findUserProfile(jwt);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/{query}")
    public ResponseEntity<List<User>> searchUserProfile(@PathVariable("query") String query) throws Exception {
        List<User> users=userService.serchUserProfile(query);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<User> updateUserProfile(@RequestBody UpdateUserRequest req,
                                                  @RequestHeader("Authorization") String jwt) throws Exception {
        User user=userService.findUserProfile(jwt);
        User updatedUser=userService.updateUser(user.getId(),req);
        return new ResponseEntity<>(updatedUser, HttpStatus.ACCEPTED);
    }
}
