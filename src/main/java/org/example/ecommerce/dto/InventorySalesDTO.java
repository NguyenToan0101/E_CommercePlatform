package org.example.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventorySalesDTO {
    private Integer inventoryId;
    private String color;
    private String dimension;
    private Integer stockQuantity;
    private Integer soldQuantity;
    private BigDecimal revenue;
    private BigDecimal price;
} 