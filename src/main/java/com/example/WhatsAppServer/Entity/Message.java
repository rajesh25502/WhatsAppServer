package com.example.WhatsAppServer.Entity;

import com.example.WhatsAppServer.DTO.Emoji;
import com.example.WhatsAppServer.DTO.MessageType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private MessageType messageType;

    private String text;

    @OneToOne
    private FileMetaData fileMetaData;

    private LocalDateTime timeStamp;

    @JsonIgnore
    @ManyToOne
    private User user;

    @JsonIgnore
    @ManyToOne
    private ChatRoom chat;

    private List<Emoji> emoji=new ArrayList<>();

}
