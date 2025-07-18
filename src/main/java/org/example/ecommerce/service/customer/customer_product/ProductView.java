package org.example.ecommerce.service.customer.customer_product;

import java.math.BigDecimal;

public class ProductView {
    private Integer productId;
    private String name;
    private BigDecimal price;
    private String imageUrl;
    private String shopaddress;
    private float rate;
    private Integer categoryid;
    private String categoryname;
    private Integer solditems;

    public ProductView(Integer productId, String name, BigDecimal price, String imageUrl, String shopaddress, float rate, Integer categoryid, String categoryname, Integer solditems) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.shopaddress = shopaddress;
        this.rate = rate;
        this.categoryid = categoryid;
        this.categoryname = categoryname;
        this.solditems = solditems;
    }

    public Integer getSolditems() {
        return solditems;
    }

    public void setSolditems(Integer solditems) {
        this.solditems = solditems;
    }

    public ProductView(Integer id, String name, BigDecimal price, int totalSold, String imageUrl, String shopaddress, float rate) {
        this.productId = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.shopaddress = shopaddress;
        this.rate = rate;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getShopaddress() {
        return shopaddress;
    }

    public void setShopaddress(String shopaddress) {
        this.shopaddress = shopaddress;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public Integer getCategoryid() {
        return categoryid;
    }

    public void setCategoryid(Integer categoryid) {
        this.categoryid = categoryid;
    }

    public String getCategoryname() {
        return categoryname;
    }

    public void setCategoryname(String categoryname) {
        this.categoryname = categoryname;
    }
}
