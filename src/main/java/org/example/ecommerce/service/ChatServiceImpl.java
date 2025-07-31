package org.example.ecommerce.service;

import org.example.ecommerce.entity.Conversation;
import org.example.ecommerce.entity.Customer;
import org.example.ecommerce.entity.Message;
import org.example.ecommerce.entity.Seller;
import org.example.ecommerce.repository.ConversationRepository;
import org.example.ecommerce.repository.CustomerRepository;
import org.example.ecommerce.repository.MessageRepository;
import org.example.ecommerce.repository.seller.SellerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class ChatServiceImpl implements ChatService {
    @Autowired
    private ConversationRepository conversationRepository;
    
    @Autowired
    private MessageRepository messageRepository;
    
    @Autowired
    private CustomerRepository customerRepository;
    
    @Autowired
    private SellerRepo sellerRepository;

    @Override
    public Conversation getOrCreateConversation(Integer customerId, Integer sellerId) {
        // Tìm conversation hiện có
        return conversationRepository.findByCustomerIdAndSellerId(customerId, sellerId)
                .orElseGet(() -> {
                    // Tạo conversation mới nếu chưa có
                    Customer customer = customerRepository.findById(customerId)
                            .orElseThrow(() -> new RuntimeException("Customer not found"));
                    Seller seller = sellerRepository.findById(sellerId)
                            .orElseThrow(() -> new RuntimeException("Seller not found"));
                    
                    Conversation conversation = new Conversation();
                    conversation.setCustomerid(customer);
                    conversation.setSellerid(seller);
                    conversation.setCreatedat(Instant.now());
                    conversation.setLastmessageat(Instant.now());
                    conversation.setStatus("ACTIVE");
                    
                    return conversationRepository.save(conversation);
                });
    }

    @Override
    public List<Conversation> getCustomerConversations(Integer customerId) {
        return conversationRepository.findByCustomerId(customerId);
    }

    @Override
    public List<Conversation> getSellerConversations(Integer sellerId) {
        return conversationRepository.findBySellerId(sellerId);
    }

    @Override
    public List<Conversation> searchCustomerConversationsByShopName(Integer customerId, String shopName) {
        return conversationRepository.findByCustomerIdAndShopNameContaining(customerId, shopName);
    }

    @Override
    public List<Conversation> searchSellerConversationsByCustomerEmail(Integer sellerId, String customerEmail) {
        return conversationRepository.findBySellerIdAndCustomerEmailContaining(sellerId, customerEmail);
    }

    @Override
    public List<Message> getConversationMessages(Integer conversationId) {
        return messageRepository.findByConversationIdOrderBySentAtAsc(conversationId);
    }

    @Override
    public Message sendMessage(Integer conversationId, Integer senderId, Integer receiverId, String content) {
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new RuntimeException("Conversation not found"));
        
        Message message = new Message();
        message.setConversationid(conversation);
        message.setSenderid(senderId);
        message.setReceiverid(receiverId);
        message.setContent(content);
        message.setSentat(Instant.now());
        message.setIsread(false);
        message.setMessageType("TEXT");
        
        // Cập nhật thời gian tin nhắn cuối
        conversation.setLastmessageat(Instant.now());
        conversationRepository.save(conversation);
        
        return messageRepository.save(message);
    }

    @Override
    public Message sendImageMessage(Integer conversationId, Integer senderId, Integer receiverId, String imageUrl) {
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new RuntimeException("Conversation not found"));
        
        Message message = new Message();
        message.setConversationid(conversation);
        message.setSenderid(senderId);
        message.setReceiverid(receiverId);
        message.setContent(""); // Không có text cho ảnh
        message.setImageUrl(imageUrl);
        message.setSentat(Instant.now());
        message.setIsread(false);
        message.setMessageType("IMAGE");
        
        // Cập nhật thời gian tin nhắn cuối
        conversation.setLastmessageat(Instant.now());
        conversationRepository.save(conversation);
        
        return messageRepository.save(message);
    }

    @Override
    public void markMessagesAsRead(Integer conversationId, Integer receiverId) {
        messageRepository.markMessagesAsRead(conversationId, receiverId);
    }

    @Override
    public long countUnreadMessages(Integer conversationId, Integer receiverId) {
        return messageRepository.countUnreadMessages(conversationId, receiverId);
    }
} 