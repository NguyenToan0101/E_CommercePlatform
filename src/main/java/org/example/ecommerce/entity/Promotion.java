package org.example.ecommerce.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Nationalized;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "promotions")
@Data
public class Promotion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "promotionid")
    private Integer id;

    @Column( unique = true)
    private String code;
    @Nationalized
    private String description;


    private LocalDateTime startdate;


    private LocalDateTime enddate;


    @Nationalized
    private String name;
    @Nationalized
    private String type;


    @Column(name = "min_order_value")
    private Integer minOrderValue;

    @Column(name = "max_discount")
    private Integer maxDiscount;
    @Column(name = "usage_limit")
    private Integer usageLimit;
    @Column(name = "usage_count")
    private Integer usageCount;
    @Column(name = "per_user_limit")
    private Integer perUserLimit;
    private Double value;
    private BigDecimal revenue;
    private String status;
    private Integer orders;
    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(
            name = "promotion_category",
            joinColumns = @JoinColumn(name = "promotionid"),
            inverseJoinColumns = @JoinColumn(name = "categoryid")
    )
    private List<Category> categories;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shopid")
    private Shop shop;

    @OneToMany(mappedBy = "promotionid", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PromotionTarget> promotionTargets = new ArrayList<>();


    public Promotion() {
    }

    public enum Type{
        PERCENTAGE,
        SHIPPING,
        FIXED
    }

    public enum Status{
        ACTIVE,
        SCHEDULED,
        EXPIRED,
        PAUSED
    }
}

