package org.example.ecommerce.entity.admin;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "system_notifications")
public class SystemNotification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer notificationid;

    @ManyToOne
    @JoinColumn(name = "adminid")
    private Admin admin;

    private Integer customerid;

    private String message;

    private LocalDateTime createdat;

    private Boolean isread = false;

    // Getters and setters
}

