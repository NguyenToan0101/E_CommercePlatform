package org.example.ecommerce.common.dto.analysis;

import lombok.Data;

@Data
public class YearlyRevenueDTO {
    private String year;
    private long revenue;
    private long profit;
    private long cost;
}

