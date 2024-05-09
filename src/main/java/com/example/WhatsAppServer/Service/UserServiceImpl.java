package com.example.WhatsAppServer.Service;

import com.example.WhatsAppServer.Config.JWTProvider;
import com.example.WhatsAppServer.DTO.UpdateUserRequest;
import com.example.WhatsAppServer.Entity.User;
import com.example.WhatsAppServer.Exception.UserException;
import com.example.WhatsAppServer.Repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private JWTProvider jwtProvider;

    @Override
    public User findUserProfile(String jwt) throws UserException {

        String email=jwtProvider.getEmailFromJWTToken(jwt);

        if(email==null)
        {
            throw new BadCredentialsException("Invalid token.... ");

        }
        User user=userRepo.findByEmail(email);
        if(user==null){
            throw new UserException("User not found  ");

        }
        return user;
    }
    @Override
    public User findUserByID(Integer id) throws UserException {

        Optional<User> user=userRepo.findById(id);
        if(user.isPresent()){
            return user.get();
        }
        throw new UserException("User not found with "+id);
    }


    @Override
    public List<User> serchUserProfile(String query) {
        return userRepo.searchUser(query);
    }
    @Override
    public User updateUser(Integer userId, UpdateUserRequest req) throws UserException {
        User user=findUserByID(userId);
        if(req.getName()!=null)
        {
            user.setName(req.getName());
        }
        if(req.getProfilePicture()!=null)
        {
            user.setProfilePicture(req.getProfilePicture());
        }
        return userRepo.save(user);
    }


}
