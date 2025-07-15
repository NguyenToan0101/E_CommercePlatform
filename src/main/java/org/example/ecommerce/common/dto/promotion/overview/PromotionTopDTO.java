package org.example.ecommerce.common.dto.promotion.overview;

import lombok.Data;

import java.math.BigDecimal;
@Data
public class PromotionTopDTO {
    private String name;
    private int orders;
    private BigDecimal revenue;
    private Double conversion;
    private Double ROI;
    public PromotionTopDTO(String name, int orders, BigDecimal revenue, Double conversion,Double ROI) {
        this.name = name;
        this.orders = orders;
        this.revenue = revenue;
        this.conversion = conversion;
        this.ROI = ROI;
    }
}

