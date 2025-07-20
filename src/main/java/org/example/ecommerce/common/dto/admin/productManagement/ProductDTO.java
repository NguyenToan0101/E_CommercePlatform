package org.example.ecommerce.common.dto.admin.productManagement;

import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Data
public class ProductDTO {
    private Integer   id;
    private String    name;
    private String    shopName;
    private String    categoryName;
    private String    status;
    private Instant createdat;
    private BigDecimal price;
    private Integer    sold;
    private String     thumbnail;


    public ProductDTO(Integer id, String name, String shopName, String categoryName, String status, Instant createdat, BigDecimal price, Integer sold, String thumbnail) {
        this.id = id;
        this.name = name;
        this.shopName = shopName;
        this.categoryName = categoryName;
        this.status = status;
        this.createdat = createdat;
        this.price = price;
        this.sold = sold;
        this.thumbnail = thumbnail;
    }
}
