package org.example.ecommerce.entity.conplaint;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "complaint_product")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ComplaintProduct {
    @Id
    @Column(name = "complaint_id")
    private Integer complaintId;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "complaint_id")
    private Complaint complaint;

    @Column(name="product_id", nullable = false)
    private Integer product_id;
}
