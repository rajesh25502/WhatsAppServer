package com.example.WhatsAppServer.Service;

import com.example.WhatsAppServer.DTO.UpdateUserRequest;
import com.example.WhatsAppServer.Entity.User;
import com.example.WhatsAppServer.Exception.UserException;

import java.util.List;

public interface UserService {

    public User findUserProfile(String jwt) throws UserException;
    public User findUserByID(Integer id) throws UserException;

    public List<User> serchUserProfile(String query);
    public User updateUser(Integer userId, UpdateUserRequest req)throws UserException;

}
