package org.example.ecommerce.common.dto.seller.dashboard;

import lombok.Data;

import java.math.BigDecimal;

@Data
public  class MonthlyRevenueDTO {
    private String month;
    private BigDecimal revenue;
}
