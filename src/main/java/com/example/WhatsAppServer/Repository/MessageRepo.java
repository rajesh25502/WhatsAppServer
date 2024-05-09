package com.example.WhatsAppServer.Repository;

import com.example.WhatsAppServer.Entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepo extends JpaRepository<Message,Integer> {

    @Query("Select m from Message m Join m.chat c where c.id=:chatId")
    public List<Message> findByChatId(@Param("chatId")Integer chatId);
}
