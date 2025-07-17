package org.example.ecommerce.common.dto.dashboard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RealTimeActivityDTO {
    private String time;
    private long users;
    private long orders;
    private BigDecimal revenue;


}

