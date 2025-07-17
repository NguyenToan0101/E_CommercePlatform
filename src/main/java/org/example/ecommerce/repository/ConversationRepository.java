package org.example.ecommerce.repository;

import org.example.ecommerce.entity.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Integer> {
    
    // Tìm conversation giữa customer và seller
    @Query("SELECT c FROM Conversation c WHERE c.customerid.id = :customerId AND c.sellerid.id = :sellerId")
    Optional<Conversation> findByCustomerIdAndSellerId(@Param("customerId") Integer customerId, 
                                                      @Param("sellerId") Integer sellerId);
    
    // Lấy tất cả conversation của customer
    @Query("SELECT c FROM Conversation c WHERE c.customerid.id = :customerId ORDER BY c.lastmessageat DESC")
    List<Conversation> findByCustomerId(@Param("customerId") Integer customerId);
    
    // Lấy tất cả conversation của seller
    @Query("SELECT c FROM Conversation c WHERE c.sellerid.id = :sellerId ORDER BY c.lastmessageat DESC")
    List<Conversation> findBySellerId(@Param("sellerId") Integer sellerId);
    
    // Tìm conversation theo tên shop (cho customer)
    @Query("SELECT c FROM Conversation c WHERE c.customerid.id = :customerId AND c.sellerid.shop.shopname LIKE %:shopName% ORDER BY c.lastmessageat DESC")
    List<Conversation> findByCustomerIdAndShopNameContaining(@Param("customerId") Integer customerId, 
                                                            @Param("shopName") String shopName);
    
    // Tìm conversation theo email customer (cho seller)
    @Query("SELECT c FROM Conversation c WHERE c.sellerid.id = :sellerId AND c.customerid.email LIKE %:customerEmail% ORDER BY c.lastmessageat DESC")
    List<Conversation> findBySellerIdAndCustomerEmailContaining(@Param("sellerId") Integer sellerId, 
                                                               @Param("customerEmail") String customerEmail);
}