package org.example.ecommerce.common.dto.dashboard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegionPerformanceDTO {
    private String region;
    private BigDecimal revenue;
    private int orders;


    public RegionPerformanceDTO(String hàNội, long l, int i, int i1) {
    }
}
