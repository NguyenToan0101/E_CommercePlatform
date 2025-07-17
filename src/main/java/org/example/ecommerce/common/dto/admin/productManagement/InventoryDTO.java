package org.example.ecommerce.common.dto.admin.productManagement;

import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Data
public class InventoryDTO {
    private Integer soldItems;
    private BigDecimal price;
    private String color;
    private String dimension;
    private Instant updatedAt;

    public InventoryDTO(Integer soldItems, BigDecimal price, String color, String dimension, Instant updatedAt) {
        this.soldItems = soldItems;
        this.price = price;
        this.color = color;
        this.dimension = dimension;
        this.updatedAt = updatedAt;
    }
}
