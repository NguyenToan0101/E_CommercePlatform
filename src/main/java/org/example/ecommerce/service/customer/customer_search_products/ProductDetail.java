package org.example.ecommerce.service.customer.customer_search_products;

import org.example.ecommerce.entity.*;

import java.math.BigDecimal;
import java.util.List;

public class ProductDetail {
    private Product product;
    private Shop shop;
    private List<Inventory> inventories;
    private List<Productimage> images;
    private List<Review> reviews;
    private List<Wishlist> wishlists;
    private BigDecimal price;
    private  float rate;
    private Integer solditems;
    private Integer sumreview;

    public ProductDetail(Product product, Shop shop, List<Inventory> inventories, List<Productimage> images, List<Review> reviews, List<Wishlist> wishlists, BigDecimal price, float rate, Integer solditems, Integer sumreview) {
        this.product = product;
        this.shop = shop;
        this.inventories = inventories;
        this.images = images;
        this.reviews = reviews;
        this.wishlists = wishlists;
        this.price = price;
        this.rate = rate;
        this.solditems = solditems;
        this.sumreview = sumreview;
    }

    public Integer getSolditems() {
        return solditems;
    }

    public void setSolditems(Integer solditems) {
        this.solditems = solditems;
    }

    public Integer getSumreview() {
        return sumreview;
    }

    public void setSumreview(Integer sumreview) {
        this.sumreview = sumreview;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public List<Inventory> getInventories() {
        return inventories;
    }

    public void setInventories(List<Inventory> inventories) {
        this.inventories = inventories;
    }

    public List<Productimage> getImages() {
        return images;
    }

    public void setImages(List<Productimage> images) {
        this.images = images;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public List<Wishlist> getWishlists() {
        return wishlists;
    }

    public void setWishlists(List<Wishlist> wishlists) {
        this.wishlists = wishlists;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
