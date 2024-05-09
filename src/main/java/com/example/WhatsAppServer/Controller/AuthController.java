package com.example.WhatsAppServer.Controller;

import com.example.WhatsAppServer.Config.JWTProvider;
import com.example.WhatsAppServer.DTO.AuthResponse;
import com.example.WhatsAppServer.DTO.LoginRequest;
import com.example.WhatsAppServer.Entity.User;
import com.example.WhatsAppServer.Repository.UserRepo;
import com.example.WhatsAppServer.Service.CustomUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JWTProvider jwtProvider;

    @Autowired
    private CustomUserService customUserService;


    @PostMapping("/signUp")
    public ResponseEntity<AuthResponse> createUserProfile(@RequestBody User user) throws Exception {
        User isEmailExist=userRepo.findByEmail(user.getEmail());
        if(isEmailExist!=null)
        {
            throw new Exception("Email has already registered with account");
        }

        User newUser=new User();
        newUser.setEmail(user.getEmail());
        newUser.setName(user.getName());
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));

        User savedUser=userRepo.save(newUser);

        Authentication authentication=new UsernamePasswordAuthenticationToken(user.getEmail(),user.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt=jwtProvider.generateToken(authentication);

        AuthResponse authResponse=new AuthResponse();
        authResponse.setJwt(jwt);
        authResponse.setMessage("Successfully Registered .....");
        authResponse.setAuth(true);

        return new ResponseEntity<>(authResponse, HttpStatus.CREATED);
    }

    @PostMapping("/signIn")
    public ResponseEntity<AuthResponse> signIn(@RequestBody LoginRequest req)
    {
        String userName= req.getEmail();;
        String password=req.getPassword();
        Authentication authentication=authenticate(userName,password);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt=jwtProvider.generateToken(authentication);

        AuthResponse authResponse=new AuthResponse();
        authResponse.setJwt(jwt);
        authResponse.setMessage("Successfully Logged in.....");
        authResponse.setAuth(true);

        return new ResponseEntity<>(authResponse, HttpStatus.OK);
    }

    private Authentication authenticate(String userName, String password) {
        UserDetails userDetails=customUserService.loadUserByUsername(userName);

        if(userDetails==null)
        {
            throw new BadCredentialsException("Invalid username.....");
        }
        if(!passwordEncoder.matches(password,userDetails.getPassword()))
        {
            throw new BadCredentialsException("Invalid password......");
        }

        return new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
    }
}

