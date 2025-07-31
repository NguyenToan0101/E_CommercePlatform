package org.example.ecommerce.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "messages")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "messageid", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conversationid")
    private Conversation conversationid;

    @Column(name = "senderid")
    private Integer senderid;

    @Column(name = "receiverid")
    private Integer receiverid;

    @NotNull
    @Column(name = "content", nullable = false, length = Integer.MAX_VALUE)
    private String content;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "sentat")
    private Instant sentat;

    @ColumnDefault("false")
    @Column(name = "isread")
    private Boolean isread;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "message_type")
    private String messageType; // "TEXT" or "IMAGE"

}