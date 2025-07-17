package org.example.ecommerce.entity.conplaint;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "complaint_shipping")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ComplaintShipping {
    @Id
    @Column(name = "complaint_id")
    private Integer complaintId;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "complaint_id")
    private Complaint complaint;

    @Column(name = "orderid", nullable = false)
    private Integer orderId;
}
