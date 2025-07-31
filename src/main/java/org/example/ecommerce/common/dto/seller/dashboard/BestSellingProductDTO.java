package org.example.ecommerce.common.dto.seller.dashboard;

import lombok.Data;

import java.math.BigDecimal;

@Data
public  class BestSellingProductDTO {
    private String productName;
    private String productImage;
    private Long quantitySold;
    private BigDecimal totalRevenue;
    private Double percentageChange;
}
