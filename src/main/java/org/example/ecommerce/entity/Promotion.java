package org.example.ecommerce.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "promotions")
public class Promotion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "promotionid", nullable = false)
    private Integer id;

    @NotNull
    @Column(name = "code", nullable = false, length = Integer.MAX_VALUE)
    private String code;

    @Column(name = "description", length = Integer.MAX_VALUE)
    private String description;

    @NotNull
    @Column(name = "startdate", nullable = false)
    private LocalDate startdate;

    @NotNull
    @Column(name = "enddate", nullable = false)
    private LocalDate enddate;

    @ColumnDefault("false")
    @Column(name = "isglobal")
    private Boolean isglobal;

    @Column(name = "name", length = Integer.MAX_VALUE)
    private String name;

    @Column(name = "type", length = Integer.MAX_VALUE)
    private String type;

    @Column(name = "quantity")
    private Long quantity;

    @Column(name = "min_order_amount")
    private BigDecimal minOrderAmount;

    @Column(name = "max_discount_amount")
    private BigDecimal maxDiscountAmount;

    @Column(name = "usage")
    private Integer usage;

    @Column(name = "per_user_limit")
    private Integer perUserLimit;

    @Column(name = "value")
    private BigDecimal value;

    @Column(name = "effective")
    private BigDecimal effective;

    @OneToMany(mappedBy = "promotionid")
    private Set<PromotionTarget> promotionTargets = new LinkedHashSet<>();

}