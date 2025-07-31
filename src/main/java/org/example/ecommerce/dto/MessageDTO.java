package org.example.ecommerce.dto;

import java.time.Instant;

public class MessageDTO {
    private Integer id;
    private Integer senderid;
    private Integer receiverid;
    private String content;
    private Instant sentat;
    private Boolean isread;
    private String imageUrl;
    private String messageType;

    // Getters and setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Integer getSenderid() { return senderid; }
    public void setSenderid(Integer senderid) { this.senderid = senderid; }
    public Integer getReceiverid() { return receiverid; }
    public void setReceiverid(Integer receiverid) { this.receiverid = receiverid; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public Instant getSentat() { return sentat; }
    public void setSentat(Instant sentat) { this.sentat = sentat; }
    public Boolean getIsread() { return isread; }
    public void setIsread(Boolean isread) { this.isread = isread; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public String getMessageType() { return messageType; }
    public void setMessageType(String messageType) { this.messageType = messageType; }
} 