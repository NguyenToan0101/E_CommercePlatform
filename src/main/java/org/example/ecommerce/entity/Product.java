package org.example.ecommerce.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;


@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "productid", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shopid")
    private Shop shopid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoryid")
    private Category categoryid;

    @NotNull
    @Column(name = "name", nullable = false, length = Integer.MAX_VALUE)
    private String name;

    @Column(name = "description", length = Integer.MAX_VALUE)
    private String description;

    @Column(name = "status", length = Integer.MAX_VALUE)
    private String status;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "createdat")
    private Instant createdat;

    @OneToMany(mappedBy = "productid")
    private Set<Cartitem> cartitems = new LinkedHashSet<>();

    @OneToMany(mappedBy = "productid")
    private Set<Inventory> inventories = new LinkedHashSet<>();

    @OneToMany(mappedBy = "productid")
    private Set<Orderitem> orderitems = new LinkedHashSet<>();

    @OneToMany(mappedBy = "productid")
    private Set<Productimage> productimages = new LinkedHashSet<>();

    @OneToMany(mappedBy = "productid")
    private Set<PromotionTarget> promotionTargets = new LinkedHashSet<>();

    @OneToMany(mappedBy = "productid")
    private Set<Review> reviews = new LinkedHashSet<>();

    @OneToMany(mappedBy = "productid")
    private Set<Wishlist> wishlists = new LinkedHashSet<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Shop getShopid() {
        return shopid;
    }

    public void setShopid(Shop shopid) {
        this.shopid = shopid;
    }

    public Category getCategoryid() {
        return categoryid;
    }

    public void setCategoryid(Category categoryid) {
        this.categoryid = categoryid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Instant getCreatedat() {
        return createdat;
    }

    public void setCreatedat(Instant createdat) {
        this.createdat = createdat;
    }

    public Set<Cartitem> getCartitems() {
        return cartitems;
    }

    public void setCartitems(Set<Cartitem> cartitems) {
        this.cartitems = cartitems;
    }

    public Set<Inventory> getInventories() {
        return inventories;
    }

    public void setInventories(Set<Inventory> inventories) {
        this.inventories = inventories;
    }

    public Set<Orderitem> getOrderitems() {
        return orderitems;
    }

    public void setOrderitems(Set<Orderitem> orderitems) {
        this.orderitems = orderitems;
    }

    public Set<Productimage> getProductimages() {
        return productimages;
    }

    public void setProductimages(Set<Productimage> productimages) {
        this.productimages = productimages;
    }

    public Set<PromotionTarget> getPromotionTargets() {
        return promotionTargets;
    }

    public void setPromotionTargets(Set<PromotionTarget> promotionTargets) {
        this.promotionTargets = promotionTargets;
    }

    public Set<Review> getReviews() {
        return reviews;
    }

    public void setReviews(Set<Review> reviews) {
        this.reviews = reviews;
    }

    public Set<Wishlist> getWishlists() {
        return wishlists;
    }

    public void setWishlists(Set<Wishlist> wishlists) {
        this.wishlists = wishlists;
    }
}