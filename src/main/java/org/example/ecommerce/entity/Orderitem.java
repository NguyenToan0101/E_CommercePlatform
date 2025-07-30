package org.example.ecommerce.entity;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Set;


@Entity
@Table(name = "orderitems")
@Getter
@Setter
public class Orderitem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "orderitemid", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orderid")
    private Order orderid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "productid")
    private Product productid;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "unitprice")
    private BigDecimal unitprice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inventoryid")
    private Inventory inventoryid;

    @Column(name = "promotionid")
    private Integer promotionid;

    @OneToMany(mappedBy = "orderitemsid")
    private Set<Review> reviews = new LinkedHashSet<>();



}