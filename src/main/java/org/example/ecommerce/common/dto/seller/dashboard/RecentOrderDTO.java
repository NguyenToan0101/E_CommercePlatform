package org.example.ecommerce.common.dto.seller.dashboard;

import lombok.Data;

import java.math.BigDecimal;

@Data
public  class RecentOrderDTO {
    private String orderId;
    private String customerName;
    private BigDecimal amount;
    private String status;
}
