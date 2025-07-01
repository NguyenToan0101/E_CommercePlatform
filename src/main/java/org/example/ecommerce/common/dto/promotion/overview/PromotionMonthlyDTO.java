package org.example.ecommerce.common.dto.promotion.overview;

import lombok.Data;

import java.math.BigDecimal;
@Data
public class PromotionMonthlyDTO {
    private String month;
    private BigDecimal revenue;
    private int orders;
    private BigDecimal discount;

    public PromotionMonthlyDTO(String month, BigDecimal revenue, int orders, BigDecimal discount) {
        this.month = month;
        this.revenue = revenue;
        this.orders = orders;
        this.discount = discount;
    }
}

