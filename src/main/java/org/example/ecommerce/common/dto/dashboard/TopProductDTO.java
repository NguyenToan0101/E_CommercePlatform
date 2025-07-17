package org.example.ecommerce.common.dto.dashboard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TopProductDTO {
    private String name;
    private BigDecimal value;
    private String category;


}

