package org.example.ecommerce.entity.conplaint;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "complaint_feedback")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ComplaintFeedback {
    @Id
    @Column(name = "complaint_id")
    private Integer complaintId;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "complaint_id")
    private Complaint complaint;

    @Column(name = "review_id", nullable = false)
    private Integer reviewId;
}
