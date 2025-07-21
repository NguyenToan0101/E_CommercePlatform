package org.example.ecommerce.common.dto.admin.complaintManagement;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ComplaintListDTO {
    private Integer complaintId;
    private Integer customerId;
    private String firstname;
    private String lastname;
    private String email;
    private String phone;
    private String reasonDescription;
    private Integer rawProductId;
    private Integer rawReviewId;
    private Integer rawPaymentId;
    private Integer rawShippingOrderId;
    private String categoryName;
    private String status;
    private LocalDateTime createdAt;

    public ComplaintListDTO(
            Integer complaintId,
            Integer customerId,
            String firstname,
            String lastname,
            String email,
            String phone,
            String reasonDescription,
            Integer rawProductId,
            Integer rawReviewId,
            Integer rawPaymentId,
            Integer rawShippingOrderId,
            String categoryName,
            String status,
            LocalDateTime createdAt
    ) {
        this.complaintId       = complaintId;
        this.customerId        = customerId;
        this.firstname         = firstname;
        this.lastname          = lastname;
        this.email             = email;
        this.phone             = phone;
        this.reasonDescription = reasonDescription;
        this.rawProductId      = rawProductId;
        this.rawReviewId       = rawReviewId;
        this.rawPaymentId      = rawPaymentId;
        this.rawShippingOrderId= rawShippingOrderId;
        this.categoryName      = categoryName;
        this.status            = status;
        this.createdAt         = createdAt;
    }

}
