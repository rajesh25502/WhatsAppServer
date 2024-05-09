package com.example.WhatsAppServer.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/home")
public class HomeController {

    @GetMapping()
    public ResponseEntity<String> hemoPage()
    {
        return new ResponseEntity<>("Up and running", HttpStatus.OK);
    }
}
