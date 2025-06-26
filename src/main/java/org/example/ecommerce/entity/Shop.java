package org.example.ecommerce.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "shop")
@Getter
@Setter
public class Shop {

    @Id
    @Column(name = "shopid")
    private Integer id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "shopid")
    private Seller seller;

    @Column(name = "shopname")
    private String shopname;

    @Column(name = "description")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maincategoryid")
    private Category maincategoryid;

    @Column(name = "fulladdress")
    private String fulladdress;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "createdat")
    private Instant createdat;

    @Column(name = "status")
    private String status;

    @Column(name = "imageshop")
    private String imageshop;

    @Column(name = "managename")
    private String managename;

    @Column(name = "phone")
    private String phone;

    @Column(name = "express")
    private Boolean express;

    @Column(name = "fast")
    private Boolean fast;

    @Column(name = "economy")
    private Boolean economy;

    @Column(name = "lockerdelivery")
    private Boolean lockerdelivery;

    @Column(name = "bulkyitems")
    private Boolean bulkyitems;

    @Column(name = "businesstype")
    private String businesstype;

    @Column(name = "businessaddress")
    private String businessaddress;

    @Column(name = "invoiceemail")
    private String invoiceemail;

    @Column(name = "taxcode")
    private String taxcode;

    @Column(name = "version")
    private Long version;

    @OneToMany(mappedBy = "shopid")
    private Set<Product> products = new LinkedHashSet<>();

    @OneToMany(mappedBy = "shopid")
    private Set<PromotionTarget> promotionTargets = new LinkedHashSet<>();

    public Shop() {
    }

    public Shop(Integer id, String status) {
        this.id = id;
        this.status = status;
    }

    public enum Status {
        PENDING_APPROVAL,
        ACTIVE,
        LOCK
    }
}
