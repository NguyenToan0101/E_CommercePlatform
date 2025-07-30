package org.example.ecommerce.service.customer.customer_product;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
@Getter
@Setter
public class ProductView {
    private Integer productId;
    private String name;
    private BigDecimal price;
    private String imageUrl;
    private String shopaddress;
    private float rate;
    private Integer solditems;

    public ProductView(Integer productId, String name, BigDecimal price, String imageUrl, String shopaddress, float rate, Integer solditems) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.shopaddress = shopaddress;
        this.rate = rate;
        this.solditems = solditems;
    }
}
