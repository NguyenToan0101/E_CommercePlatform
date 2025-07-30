package org.example.ecommerce.common.dto;

import lombok.Data;

@Data
public class ConversationDTO {
    private Integer id;
    private String customerName;
    private String sellerName;
    private String lastMessage;
    private String timeAgo;
    private int unreadCount;
}
