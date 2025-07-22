package org.example.ecommerce.controller;

import org.example.ecommerce.entity.Message;
import org.example.ecommerce.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.time.Instant;
import java.util.Map;
import org.example.ecommerce.dto.MessageDTO;

@Controller
public class ChatWebSocketController {
    @Autowired
    private ChatService chatService;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    // Nhận tin nhắn từ client gửi tới /app/chat.send/{conversationId}
    @MessageMapping("/chat.send/{conversationId}")
    public void sendMessage(@DestinationVariable Integer conversationId, @Payload Map<String, Object> payload) {
        Integer senderId = (Integer) payload.get("senderid");
        Integer receiverId = (Integer) payload.get("receiverid");
        String content = (String) payload.get("content");
        // Lưu message vào DB
        Message message = chatService.sendMessage(conversationId, senderId, receiverId, content);
        // Chuyển sang DTO
        MessageDTO dto = new MessageDTO();
        dto.setId(message.getId());
        dto.setSenderid(message.getSenderid());
        dto.setReceiverid(message.getReceiverid());
        dto.setContent(message.getContent());
        dto.setSentat(message.getSentat());
        dto.setIsread(message.getIsread());
        // Gửi message DTO tới tất cả client subscribe /topic/conversation/{conversationId}
        messagingTemplate.convertAndSend("/topic/conversation/" + conversationId, dto);
    }
} 