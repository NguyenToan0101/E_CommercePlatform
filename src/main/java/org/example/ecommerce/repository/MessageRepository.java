package org.example.ecommerce.repository;

import org.example.ecommerce.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Integer> {
    List<Message> findByConversationid_IdOrderBySentatAsc(Integer conversationId);

    @Query("SELECT COUNT(m) FROM Message m WHERE m.conversationid.id = :conversationId AND m.receiverid = :userId AND m.isread = false")
    int countUnreadMessages(@Param("conversationId") Integer conversationId, @Param("userId") Integer userId);

    @Query("SELECT m.content FROM Message m WHERE m.conversationid.id = :conversationId ORDER BY m.sentat DESC LIMIT 1")
    String findLastMessage(@Param("conversationId") Integer conversationId);
    
    @Modifying
    @Query("UPDATE Message m SET m.isread = true WHERE m.conversationid.id = :conversationId AND m.receiverid = :userId AND m.isread = false")
    void markMessagesAsRead(@Param("conversationId") Integer conversationId, @Param("userId") Integer userId);
}