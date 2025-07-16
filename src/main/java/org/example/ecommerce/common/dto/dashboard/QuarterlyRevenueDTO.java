package org.example.ecommerce.common.dto.dashboard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuarterlyRevenueDTO {
    private String quarter;
    private long revenue;
    private double growth;
    private int orders;
    private int avgOrder;


}

