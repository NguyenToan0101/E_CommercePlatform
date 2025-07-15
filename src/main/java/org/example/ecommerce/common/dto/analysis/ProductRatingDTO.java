package org.example.ecommerce.common.dto.analysis;

import lombok.Data;

@Data
public class ProductRatingDTO {
    private String rating;
    private int count;
    private double percentage;
}
