package org.example.ecommerce.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "conversations")
public class Conversation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "conversationid", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customerid")
    private Customer customerid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sellerid")
    private Seller sellerid;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "createdat")
    private Instant createdat;

    @Column(name = "lastmessageat")
    private Instant lastmessageat;

    @Column(name = "status", length = Integer.MAX_VALUE)
    private String status;

    @OneToMany(mappedBy = "conversationid")
    private Set<Message> messages = new LinkedHashSet<>();

}