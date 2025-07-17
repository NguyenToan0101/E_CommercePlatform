package org.example.ecommerce.entity.conplaint;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "complaint")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Complaint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "complaint_id")
    private Integer complaintId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    private org.example.ecommerce.entity.Customer customer;

    @Column(name = "order_id", nullable = true)
    private Integer orderId;         // tham chiếu đến orders.orderid

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    private ComplaintCategory category;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "reason_id", nullable = false)
    private ComplaintReason reason;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;      // mô tả chi tiết (có thể null)

    @Column(name = "status", nullable = false, length = 20)
    private String status = "pending";

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();


    //test
    // Mỗi complaint có thể có 1 product
    @OneToOne(mappedBy = "complaint", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private ComplaintProduct product;

    // Mỗi complaint có thể có 1 feedback
    @OneToOne(mappedBy = "complaint", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private ComplaintFeedback feedback;
}
