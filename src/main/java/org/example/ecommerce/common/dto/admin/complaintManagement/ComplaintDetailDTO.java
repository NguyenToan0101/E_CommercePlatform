package org.example.ecommerce.common.dto.admin.complaintManagement;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ComplaintDetailDTO {
    // chung
    private Integer complaintId;
    private Integer customerId;
    private String customerName;
    private String firstname;
    private String lastname;
    private String email;
    private String phone;
    private String reasonDescription;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private String categoryName;
    private String status;

    // từng nhóm
    private ProductInfo product;      // nếu category=product
    private ReviewInfo review;
    private PaymentInfo payment;
    private ShippingInfo shipping;

    // ảnh chung (payment & shipping)
    private List<String> complaintImages;
}
