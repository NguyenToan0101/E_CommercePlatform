package org.example.ecommerce.common.dto.seller.dashboard;

import lombok.Data;

import java.math.BigDecimal;

@Data
public  class CategoryRevenueDTO {
    private String categoryName;
    private BigDecimal revenue;
    private Double percentage;
    private String color;
}
