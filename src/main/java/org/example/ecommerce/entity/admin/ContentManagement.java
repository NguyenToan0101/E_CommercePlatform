package org.example.ecommerce.entity.admin;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "content_management")
public class ContentManagement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer contentid;

    private String title;

    private String content;

    @ManyToOne
    @JoinColumn(name = "updated_by")
    private Admin updatedBy;

    private LocalDateTime updatedAt;

    // Getters and setters
}

