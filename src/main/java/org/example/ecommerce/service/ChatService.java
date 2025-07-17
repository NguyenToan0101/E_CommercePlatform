package org.example.ecommerce.service;

import org.example.ecommerce.entity.Conversation;
import org.example.ecommerce.entity.Message;
import java.util.List;

public interface ChatService {
    Conversation createConversation(Integer customerId, Integer sellerId);
    List<Conversation> getConversationsForSeller(Integer sellerId);
    List<Conversation> getConversationsForCustomer(Integer customerId);
    List<Conversation> searchConversationsForSeller(Integer sellerId, String keyword);
    List<Conversation> searchConversationsForCustomer(Integer customerId, String keyword);
    Message sendMessage(Integer conversationId, Integer senderId, Integer receiverId, String content);
    List<Message> getMessages(Integer conversationId);
    void markMessagesAsRead(Integer conversationId, Integer userId);
    int countUnreadMessages(Integer conversationId, Integer userId);
} 