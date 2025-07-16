package org.example.ecommerce.common.dto.analysis;

import lombok.Data;

@Data
public class UserRegionDTO {
    private String region;
    private int users;
    private double percentage;
}

