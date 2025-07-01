package org.example.ecommerce.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.annotations.Nationalized;

import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.LinkedHashSet;
import java.util.Set;


@Entity
@Table(name = "products")
@Data
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "productid", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shopid")
    private Shop shopid;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "categoryid")
//    private Category categoryid;

    @Size(max = 100)
    @Nationalized
    @Column(name = "name", length = 100)
    private String name;

    @Nationalized
//    @Lob
    @Column(name = "description")
    private String description;

    @Column(name = "price", precision = 18, scale = 2)
    private BigDecimal price;

    @Size(max = 20)
    @Column(name = "status", length = 20)
    private String status;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "createdat")
    private Instant createdat;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoryid")
    private Category categoryid;




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

}