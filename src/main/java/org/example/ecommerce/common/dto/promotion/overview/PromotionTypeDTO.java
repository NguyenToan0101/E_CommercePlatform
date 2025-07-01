package org.example.ecommerce.common.dto.promotion.overview;

import lombok.Data;

@Data
public class PromotionTypeDTO {
    private String name;
    private float value;
    private String color;

    public PromotionTypeDTO(String name, float value, String color) {
        this.name = name;
        this.value = value;
        this.color = color;
    }
}
