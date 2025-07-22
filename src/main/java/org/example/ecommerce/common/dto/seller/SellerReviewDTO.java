package org.example.ecommerce.common.dto.seller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SellerReviewDTO {
    private Integer reviewId;
    private Integer productId;
    private String productName;
    private String productImage; // thumbnail
    private Integer rating;
    private String comment;
    private List<String> reviewImages;
    private Instant createdAt;
    private String customerName;
} 