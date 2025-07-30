package org.example.ecommerce.entity.conplaint;

import jakarta.persistence.*;
import lombok.*;
import org.example.ecommerce.entity.Customer;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "complaint")
public class Complaint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "complaint_id")
    private Integer complaintId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Column(name = "order_id", nullable = true)
    private Integer orderId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    private ComplaintCategory category;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "reason_id", nullable = false)
    private ComplaintReason reason;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

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
    private ComplaintReview review;

    @OneToOne(mappedBy = "complaint", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private ComplaintPayment payment;

    @OneToOne(mappedBy = "complaint", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private ComplaintShipping shipping;

    @OneToMany(mappedBy = "complaint", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ComplaintImg> images = new ArrayList<>();

}
