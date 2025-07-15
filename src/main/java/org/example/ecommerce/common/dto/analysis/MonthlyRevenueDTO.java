package org.example.ecommerce.common.dto.analysis;

import lombok.Data;

@Data
public class MonthlyRevenueDTO {
    private String month;
    private long revenue;
    private long profit;
    private long cost;
}
