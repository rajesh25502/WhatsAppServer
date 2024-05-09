package com.example.WhatsAppServer.Repository;

import com.example.WhatsAppServer.Entity.ChatRoom;
import com.example.WhatsAppServer.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRoomRepo extends JpaRepository<ChatRoom,Integer> {

    @Query("SELECT c FROM ChatRoom c JOIN c.users u where u.id=:userId")
    public List<ChatRoom> findChatByUserId(Integer userId);

    @Query("SELECT c FROM ChatRoom c WHERE c.isGroup=false AND :user Member of c.users AND :regUser Member of c.users")
    public ChatRoom findChatByIds(@Param("user") User user,@Param("regUser") User regUser);

}
