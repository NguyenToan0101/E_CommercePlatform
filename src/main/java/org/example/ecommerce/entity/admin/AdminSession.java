package org.example.ecommerce.entity.admin;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "admin_sessions")
@Data
public class AdminSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer sessionid;

    @ManyToOne
    @JoinColumn(name = "adminid")
    private Admin admin;

    @Column(nullable = false, unique = true)
    private String token;

    private String ipAddress;

    private String userAgent;

    private LocalDateTime loginTime;

    private LocalDateTime logoutTime;

    // Getters and setters
}

