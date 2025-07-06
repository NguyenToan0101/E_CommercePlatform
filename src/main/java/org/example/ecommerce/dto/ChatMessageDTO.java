package org.example.ecommerce.dto;

import lombok.Data;

@Data
public class ChatMessageDTO {
    private Integer conversationId;
    private Integer senderId;
    private Integer receiverId;
    private String content;
}
