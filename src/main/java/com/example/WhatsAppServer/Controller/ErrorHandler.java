package com.example.WhatsAppServer.Controller;

import com.example.WhatsAppServer.Exception.ApplicationError;
import com.example.WhatsAppServer.Exception.ChatRoomException;
import com.example.WhatsAppServer.Exception.MessageException;
import com.example.WhatsAppServer.Exception.UserException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class ErrorHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(UserException.class)
    public ResponseEntity<ApplicationError> handleUserException(UserException exception, WebRequest webRequest)
    {
        ApplicationError error=new ApplicationError();
        error.setCode(500);
        error.setMessage(exception.getMessage());
        error.setDetails("WhatsApp api server");

        return  new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MessageException.class)
    public ResponseEntity<ApplicationError> handleMessageException(MessageException exception, WebRequest webRequest)
    {
        ApplicationError error=new ApplicationError();
        error.setCode(500);
        error.setMessage(exception.getMessage());
        error.setDetails("WhatsApp api server");

        return  new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ChatRoomException.class)
    public ResponseEntity<ApplicationError> handleChatRoomException(ChatRoomException exception, WebRequest webRequest)
    {
        ApplicationError error=new ApplicationError();
        error.setCode(500);
        error.setMessage(exception.getMessage());
        error.setDetails("WhatsApp api server");

        return  new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApplicationError> handleCommonException(Exception exception, WebRequest webRequest)
    {
        ApplicationError error=new ApplicationError();
        error.setCode(500);
        error.setMessage(exception.getMessage());
        error.setDetails("WhatsApp api server");

        return  new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
