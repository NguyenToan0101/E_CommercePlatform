package org.example.ecommerce.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "categoryid", nullable = false)
    private Integer id;

    @NotNull
    @Column(name = "categoryname", nullable = false, length = Integer.MAX_VALUE)
    private String categoryname;


    @OneToMany(mappedBy = "categoryid")
    private Set<Product> products = new LinkedHashSet<>();

    @OneToMany(mappedBy = "categoryid")
    private Set<PromotionTarget> promotionTargets = new LinkedHashSet<>();

    @OneToMany(mappedBy = "maincategoryid")
    private Set<Shop> shops = new LinkedHashSet<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCategoryname() {
        return categoryname;
    }

    public void setCategoryname(String categoryname) {
        this.categoryname = categoryname;
    }



    public Set<Product> getProducts() {
        return products;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }

    public Set<PromotionTarget> getPromotionTargets() {
        return promotionTargets;
    }

    public void setPromotionTargets(Set<PromotionTarget> promotionTargets) {
        this.promotionTargets = promotionTargets;
    }

    public Set<Shop> getShops() {
        return shops;
    }

    public void setShops(Set<Shop> shops) {
        this.shops = shops;
    }

}