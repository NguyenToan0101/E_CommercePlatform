package org.example.ecommerce.controller.seller.chat;

import lombok.RequiredArgsConstructor;
import org.example.ecommerce.entity.Customer;
import org.example.ecommerce.entity.Message;
import org.example.ecommerce.repository.CustomerRepository;
import org.example.ecommerce.repository.MessageRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageRepository messageRepository;
    private final CustomerRepository customerRepository;

    @GetMapping("/conversation/{id}")
    public List<Message> getMessages(@PathVariable Integer id) {
        return messageRepository.findByConversationid_IdOrderBySentatAsc(id);
    }
    
    @PostMapping("/conversation/{conversationId}/mark-read")
    public void markMessagesAsRead(@PathVariable Integer conversationId, @RequestParam Integer userId) {
        messageRepository.markMessagesAsRead(conversationId, userId);
    }
    
    @GetMapping("/customer/{customerId}")
    public Customer getCustomerInfo(@PathVariable Integer customerId) {
        return customerRepository.findById(customerId).orElse(null);
    }
}
