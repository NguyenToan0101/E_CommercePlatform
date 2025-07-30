package org.example.ecommerce.common.dto.dashboard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuarterlyRevenueDTO {
    private String quarter;
    private BigDecimal revenue;
    private double growth;
    private long orders;
    private BigDecimal avgOrder;


    public QuarterlyRevenueDTO(String quarter, BigDecimal revenue, long orders, BigDecimal avgOrder) {
        this.quarter = quarter;
        this.revenue = revenue;
        this.orders = orders;
        this.avgOrder = avgOrder;
    }

    public QuarterlyRevenueDTO(String quarter, long l, double growth, int orders, int i) {
    }
}

