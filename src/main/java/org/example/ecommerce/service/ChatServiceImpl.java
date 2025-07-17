package org.example.ecommerce.service;

import lombok.RequiredArgsConstructor;
import org.example.ecommerce.entity.Conversation;
import org.example.ecommerce.entity.Message;
import org.example.ecommerce.entity.Customer;
import org.example.ecommerce.entity.Seller;
import org.example.ecommerce.repository.ConversationRepository;
import org.example.ecommerce.repository.MessageRepository;
import org.example.ecommerce.repository.CustomerRepository;
import org.example.ecommerce.repository.seller.SellerRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {
    private final ConversationRepository conversationRepository;
    private final MessageRepository messageRepository;
    private final CustomerRepository customerRepository;
    private final SellerRepo sellerRepo;

    @Override
    public Conversation createConversation(Integer customerId, Integer sellerId) {
        List<Conversation> existing = conversationRepository.findByCustomerid_IdAndSellerid_Id(customerId, sellerId);
        if (!existing.isEmpty()) return existing.get(0);
        Customer customer = customerRepository.findById(customerId).orElseThrow();
        Seller seller = sellerRepo.findById(sellerId).orElseThrow();
        Conversation conv = new Conversation();
        conv.setCustomerid(customer);
        conv.setSellerid(seller);
        conv.setCreatedat(Instant.now());
        conv.setLastmessageat(Instant.now());
        conv.setStatus("active");
        return conversationRepository.save(conv);
    }

    @Override
    public List<Conversation> getConversationsForSeller(Integer sellerId) {
        return conversationRepository.findBySellerid_Id(sellerId);
    }

    @Override
    public List<Conversation> getConversationsForCustomer(Integer customerId) {
        return conversationRepository.findByCustomerid_Id(customerId);
    }

    @Override
    public List<Conversation> searchConversationsForSeller(Integer sellerId, String keyword) {
        return conversationRepository.searchConversationsForSeller(sellerId, keyword);
    }

    @Override
    public List<Conversation> searchConversationsForCustomer(Integer customerId, String keyword) {
        return conversationRepository.searchConversationsForCustomer(customerId, keyword);
    }

    @Override
    @Transactional
    public Message sendMessage(Integer conversationId, Integer senderId, Integer receiverId, String content) {
        Conversation conv = conversationRepository.findById(conversationId).orElseThrow();
        Message msg = new Message();
        msg.setConversationid(conv);
        msg.setSenderid(senderId);
        msg.setReceiverid(receiverId);
        msg.setContent(content);
        msg.setSentat(Instant.now());
        msg.setIsread(false);
        messageRepository.save(msg);
        conv.setLastmessageat(Instant.now());
        conversationRepository.save(conv);
        return msg;
    }

    @Override
    public List<Message> getMessages(Integer conversationId) {
        return messageRepository.findByConversationid_IdOrderBySentatAsc(conversationId);
    }

    @Override
    @Transactional
    public void markMessagesAsRead(Integer conversationId, Integer userId) {
        messageRepository.markMessagesAsRead(conversationId, userId);
    }

    @Override
    public int countUnreadMessages(Integer conversationId, Integer userId) {
        return messageRepository.countUnreadMessages(conversationId, userId);
    }
} 