package org.example.ecommerce.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.ecommerce.entity.admin.Admin;

import java.time.LocalDateTime;

@Entity
@Table(name = "content_management")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Content {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer contentid;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, unique = true)
    private String slug;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column
    private String type;

    @Column
    private String status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "last_updated_at") // Đổi tên cột trong CSDL
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by")
    private Admin updatedBy;
}
