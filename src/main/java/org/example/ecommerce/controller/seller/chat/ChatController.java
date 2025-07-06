package org.example.ecommerce.controller.seller.chat;

import lombok.RequiredArgsConstructor;
import org.example.ecommerce.dto.ChatMessageDTO;
import org.example.ecommerce.entity.Conversation;
import org.example.ecommerce.entity.Message;
import org.example.ecommerce.repository.ConversationRepository;
import org.example.ecommerce.repository.MessageRepository;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.Instant;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final ConversationRepository conversationRepository;
    private final MessageRepository messageRepository;
    
    @MessageMapping("/chat.send/{conversationId}")//nhan message ma user gui toi
    @SendTo("/topic/conversation/{conversationId}")//sv gui response den cac user dang conect
    public ChatMessageDTO sendToConversation(@DestinationVariable Integer conversationId, ChatMessageDTO ChatMessageDTO) {
        Conversation conv = conversationRepository.findById(conversationId).orElse(null);
        if (conv == null) return null;

        Message msg = new Message();
        msg.setConversationid(conv);
        msg.setSenderid(ChatMessageDTO.getSenderId());
        msg.setReceiverid(ChatMessageDTO.getReceiverId());
        msg.setContent(ChatMessageDTO.getContent());
        msg.setSentat(Instant.now());
        msg.setIsread(false);
        messageRepository.save(msg);

        conv.setLastmessageat(Instant.now());
        conversationRepository.save(conv);

        return ChatMessageDTO;
    }
    
    @GetMapping("/seller/chat")
    public String chatPage(Model model) {
        // TODO: Lấy sellerId từ session hoặc authentication
        // Hiện tại để tạm giá trị mặc định
        Integer sellerId = 1; // Thay bằng cách lấy từ session/authentication
        
        model.addAttribute("sellerId", sellerId);
        model.addAttribute("conversationId", 1); // Giá trị mặc định
        model.addAttribute("receiverId", 2); // Giá trị mặc định
        
        return "seller/chat/messages";
    }
}
