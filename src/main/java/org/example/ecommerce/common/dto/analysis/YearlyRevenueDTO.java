package org.example.ecommerce.common.dto.analysis;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class YearlyRevenueDTO {
    private String year;
    private BigDecimal revenue;
    private BigDecimal profit;
    private BigDecimal cost;
}

