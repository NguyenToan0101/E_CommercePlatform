package org.example.ecommerce.common.dto.analysis;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegionDTO {
    private String region;
    private int users;
    private double percentage;
}

