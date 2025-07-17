package org.example.ecommerce.common.dto.analysis;

import lombok.Data;

@Data
public class ProductCategoryDTO {
    private String category;
    private int products;
    private int sales;
    private long revenue;
}

