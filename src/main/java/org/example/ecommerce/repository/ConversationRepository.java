package org.example.ecommerce.repository;

import org.example.ecommerce.entity.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface ConversationRepository extends JpaRepository<Conversation, Integer> {
    @Query("SELECT c FROM Conversation c WHERE c.sellerid.id = :sellerId AND (LOWER(c.customerid.firstname) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(c.customerid.lastname) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<Conversation> searchConversationsForSeller(@Param("sellerId") Integer sellerId, @Param("keyword") String keyword);

    @Query("SELECT c FROM Conversation c WHERE c.customerid.id = :customerId AND (LOWER(c.sellerid.shop.shopname) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<Conversation> searchConversationsForCustomer(@Param("customerId") Integer customerId, @Param("keyword") String keyword);
} 