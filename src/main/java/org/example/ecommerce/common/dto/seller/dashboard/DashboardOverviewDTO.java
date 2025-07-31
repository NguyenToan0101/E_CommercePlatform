package org.example.ecommerce.common.dto.seller.dashboard;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class DashboardOverviewDTO {
    private BigDecimal totalRevenue;
    private Long totalOrders;
    private Long totalProducts;
    private Long totalCustomers;
    private BigDecimal conversionRate;
}
