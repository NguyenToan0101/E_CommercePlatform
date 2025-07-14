package org.example.ecommerce.controller.seller.chat;

import lombok.RequiredArgsConstructor;
import org.example.ecommerce.common.dto.ConversationDTO;
import org.example.ecommerce.entity.Conversation;
import org.example.ecommerce.entity.Customer;
import org.example.ecommerce.entity.Seller;
import org.example.ecommerce.repository.ConversationRepository;
import org.example.ecommerce.repository.CustomerRepository;
import org.example.ecommerce.repository.MessageRepository;
import org.example.ecommerce.repository.seller.SellerRepo;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/conversations")
@RequiredArgsConstructor
public class ConversationController {

    private final ConversationRepository conversationRepository;
    private final MessageRepository messageRepository;
    private final CustomerRepository customerRepository;
    private final SellerRepo sellerRepository;

    @GetMapping("/customer/{customerId}")
    public List<ConversationDTO> getConversationsForCustomer(@PathVariable Integer customerId) {
        List<Conversation> conversations = conversationRepository.findByCustomerid_Id(customerId);
        List<ConversationDTO> result = new ArrayList<>();

        for (Conversation c : conversations) {
            ConversationDTO dto = new ConversationDTO();
            dto.setId(c.getId());

            // Tên người bán
            String sellerName = c.getSellerid().getCustomer().getFirstname() + " " + c.getSellerid().getCustomer().getLastname();
            dto.setSellerName(sellerName);

            // Tin nhắn cuối
            String lastMessage = messageRepository.findLastMessage(c.getId());
            dto.setLastMessage(lastMessage != null ? lastMessage : "");

            // Thời gian (hiển thị dạng 'x phút trước')
            Instant time = c.getLastmessageat() != null ? c.getLastmessageat() : c.getCreatedat();
            dto.setTimeAgo(time != null ? timeAgo(time) : "just now");

            // Số tin chưa đọc
            int unread = messageRepository.countUnreadMessages(c.getId(), customerId);
            dto.setUnreadCount(unread);

            result.add(dto);
        }

        return result;
    }

    private String timeAgo(Instant time) {
        long minutes = Duration.between(time, Instant.now()).toMinutes();
        if (minutes < 1) return "just now";
        if (minutes < 60) return minutes + " minutes ago";
        long hours = minutes / 60;
        if (hours < 24) return hours + " hours ago";
        long days = hours / 24;
        return days + " days ago";
    }

    @GetMapping("/seller/{sellerId}")
    public List<ConversationDTO> getConversationsForSeller(@PathVariable Integer sellerId) {
        List<Conversation> conversations = conversationRepository.findBySellerid_Id(sellerId);
        List<ConversationDTO> result = new ArrayList<>();

        for (Conversation c : conversations) {
            ConversationDTO dto = new ConversationDTO();
            dto.setId(c.getId());

            // Tên khách hàng
            String customerName = c.getCustomerid().getFirstname() + " " + c.getCustomerid().getLastname();
            dto.setCustomerName(customerName);

            // Tin nhắn cuối
            String lastMessage = messageRepository.findLastMessage(c.getId());
            dto.setLastMessage(lastMessage != null ? lastMessage : "");

            // Thời gian (hiển thị dạng 'x phút trước')
            Instant time = c.getLastmessageat() != null ? c.getLastmessageat() : c.getCreatedat();
            dto.setTimeAgo(time != null ? timeAgo(time) : "just now");

            // Số tin chưa đọc
            int unread = messageRepository.countUnreadMessages(c.getId(), sellerId);
            dto.setUnreadCount(unread);

            result.add(dto);
        }

        return result;
    }
    
    @PostMapping("/create")
    public ConversationDTO createConversation(@RequestParam Integer customerId, @RequestParam Integer sellerId) {
        // Kiểm tra xem conversation đã tồn tại chưa
        List<Conversation> existingConversations = conversationRepository.findByCustomerid_IdAndSellerid_Id(customerId, sellerId);
        if (!existingConversations.isEmpty()) {
            // Nếu đã tồn tại, trả về conversation đầu tiên
            Conversation existing = existingConversations.get(0);
            ConversationDTO dto = new ConversationDTO();
            dto.setId(existing.getId());
            return dto;
        }
        
        // Tạo conversation mới
        Customer customer = customerRepository.findById(customerId).orElse(null);
        Seller seller = sellerRepository.findById(sellerId).orElse(null);
        
        if (customer == null || seller == null) {
            return null;
        }
        
        Conversation conversation = new Conversation();
        conversation.setCustomerid(customer);
        conversation.setSellerid(seller);
        conversation.setCreatedat(Instant.now());
        conversation.setStatus("active");
        
        Conversation savedConversation = conversationRepository.save(conversation);
        
        ConversationDTO dto = new ConversationDTO();
        dto.setId(savedConversation.getId());
        return dto;
    }
}
