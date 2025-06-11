package org.example.ecommerce.service.customer.customer_search_products;

import org.example.ecommerce.entity.*;

import java.util.List;

public class ProductDetail {
    private Product product;
    private Shop shop;
    private List<Inventory> inventories;
    private List<Productimage> images;
    private List<Review> reviews;
    private List<Wishlist> wishlists;

    public ProductDetail(Product product, Shop shop, List<Inventory> inventories, List<Productimage> images, List<Review> reviews, List<Wishlist> wishlists) {
        this.product = product;
        this.shop = shop;
        this.inventories = inventories;
        this.images = images;
        this.reviews = reviews;
        this.wishlists = wishlists;
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
}
