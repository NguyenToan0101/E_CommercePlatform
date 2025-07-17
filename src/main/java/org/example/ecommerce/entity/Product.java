package org.example.ecommerce.entity;

import jakarta.persistence.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.Instant;


import java.util.ArrayList;
import java.util.LinkedHashSet;

import java.util.List;
import java.util.Set;

@Getter
@Setter
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
    @NotNull(message = "Vui lòng chọn ngành hàng")
    private Category categoryid;

    @NotNull
    @NotEmpty(message = "Tên sản phẩm không được để trống")
    @Column(name = "name", nullable = false, length = Integer.MAX_VALUE)
    private String name;

    @NotEmpty(message = "Mô tả không được để trống")
    @Column(name = "description", length = Integer.MAX_VALUE)
    private String description;
    @Column(name = "price", precision = 18, scale = 2)
    private BigDecimal price;
    @Column(name = "status", length = Integer.MAX_VALUE)
    private String status;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "createdat")
    private Instant createdat;

    @Column(name = "weight")
    private Integer weight;

    @Column(name = "length")
    private Integer length;

    @Column(name = "width")
    private Integer width;

    @Column(name = "height")
    private Integer height;

    @Column(name = "use_variant_shipping")
    private Boolean useVariantShipping = false;

    @OneToMany(mappedBy = "productid")
    private Set<Cartitem> cartitems = new LinkedHashSet<>();

    @Valid
    @OneToMany(mappedBy = "productid", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Inventory> inventories = new ArrayList<>();

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

}