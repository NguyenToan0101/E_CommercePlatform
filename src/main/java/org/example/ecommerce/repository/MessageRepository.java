package org.example.ecommerce.repository;

import org.example.ecommerce.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {
    
    // Lấy tất cả message của một conversation
    @Query("SELECT m FROM Message m WHERE m.conversationid.id = :conversationId ORDER BY m.sentat ASC")
    List<Message> findByConversationIdOrderBySentAtAsc(@Param("conversationId") Integer conversationId);
    
    // Đánh dấu tin nhắn đã đọc
    @Query("UPDATE Message m SET m.isread = true WHERE m.conversationid.id = :conversationId AND m.receiverid = :receiverId AND m.isread = false")
    void markMessagesAsRead(@Param("conversationId") Integer conversationId, @Param("receiverId") Integer receiverId);
    
    // Đếm tin nhắn chưa đọc
    @Query("SELECT COUNT(m) FROM Message m WHERE m.conversationid.id = :conversationId AND m.receiverid = :receiverId AND m.isread = false")
    long countUnreadMessages(@Param("conversationId") Integer conversationId, @Param("receiverId") Integer receiverId);
} 