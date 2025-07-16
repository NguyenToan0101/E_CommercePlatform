package org.example.ecommerce.entity.conplaint;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "complaint_payment")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ComplaintPayment {
    @Id
    @Column(name = "complaint_id")
    private Integer complaintId;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "complaint_id")
    private Complaint complaint;

    @Column(name = "paymentid", nullable = false)
    private Integer paymentId;
}
