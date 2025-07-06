package org.example.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductSalesDTO {
    private Integer productId;
    private String productName;
    private String productStatus;
    private String imageUrl;
    private String categoryName;
    private String priceRange;
    private BigDecimal totalRevenue;
    private int totalSoldQuantity;
    private int totalStockQuantity;
    private List<InventorySalesDTO> inventorySales;
}
