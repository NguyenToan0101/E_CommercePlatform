package org.example.ecommerce.common.dto.analysis;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MonthlyRevenueDTO {
    private String month;
    private BigDecimal revenue;
    private BigDecimal profit;
    private BigDecimal cost;
}
