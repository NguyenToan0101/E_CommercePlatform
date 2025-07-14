package org.example.ecommerce.repository;

import org.example.ecommerce.entity.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConversationRepository extends JpaRepository<Conversation, Integer> {
    List<Conversation> findByCustomerid_Id(Integer customerId);
    List<Conversation> findBySellerid_Id(Integer sellerId);
    List<Conversation> findByCustomerid_IdAndSellerid_Id(Integer customerId, Integer sellerId);
}