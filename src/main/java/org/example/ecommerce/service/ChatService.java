package org.example.ecommerce.service;

import org.example.ecommerce.entity.Conversation;
import org.example.ecommerce.entity.Message;

import java.util.List;

public interface ChatService {
    // Tạo hoặc lấy conversation giữa customer và seller
    Conversation getOrCreateConversation(Integer customerId, Integer sellerId);
    
    // Lấy tất cả conversation của customer
    List<Conversation> getCustomerConversations(Integer customerId);
    
    // Lấy tất cả conversation của seller
    List<Conversation> getSellerConversations(Integer sellerId);
    
    // Tìm conversation theo tên shop (cho customer)
    List<Conversation> searchCustomerConversationsByShopName(Integer customerId, String shopName);
    
    // Tìm conversation theo email customer (cho seller)
    List<Conversation> searchSellerConversationsByCustomerEmail(Integer sellerId, String customerEmail);
    
    // Lấy tất cả message của conversation
    List<Message> getConversationMessages(Integer conversationId);
    
    // Gửi tin nhắn text
    Message sendMessage(Integer conversationId, Integer senderId, Integer receiverId, String content);
    
    // Gửi tin nhắn ảnh
    Message sendImageMessage(Integer conversationId, Integer senderId, Integer receiverId, String imageUrl);
    
    // Đánh dấu tin nhắn đã đọc
    void markMessagesAsRead(Integer conversationId, Integer receiverId);
    
    // Đếm tin nhắn chưa đọc
    long countUnreadMessages(Integer conversationId, Integer receiverId);
} 