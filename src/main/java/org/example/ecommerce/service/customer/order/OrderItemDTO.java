package org.example.ecommerce.service.customer.order;

import java.math.BigDecimal;

public class OrderItemDTO {
    private Integer productId;
    private String productName;
    private String imageUrl;
    private String color;
    private String dimension;
    private int quantity;
    private BigDecimal unitPrice;

    public OrderItemDTO(Integer productId, String productName, String imageUrl, String color, String dimension, int quantity, BigDecimal unitPrice) {
        this.productId = productId;
        this.productName = productName;
        this.imageUrl = imageUrl;
        this.color = color;
        this.dimension = dimension;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getDimension() {
        return dimension;
    }

    public void setDimension(String dimension) {
        this.dimension = dimension;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }
}
