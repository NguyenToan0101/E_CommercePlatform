package org.example.ecommerce.dto.promotion;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class CreatePromotionRequest {
    private String code;
    private String name;
    private String description;
    private Double value;
    private String type; // PERCENTAGE, FIXED, SHIPPING
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Integer minOrderValue;
    private Integer maxDiscount;
    private Integer usageLimit;
    private Integer perUserLimit;
    
    // For category-based promotion
    private List<Integer> categoryIds;
    
    // For product-specific promotion
    private List<Integer> productIds;
}
