package org.example.ecommerce.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "inventory")
public class Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inventoryid", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "productid")
    private Product productid;

    @NotNull
    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "solditems")
    private Integer solditems;

    @Size(max = 100)
    @Column(name = "color", length = 100)
    private String color;

    @Size(max = 100)
    @Column(name = "dimension", length = 100)
    private String dimension;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "image", length = Integer.MAX_VALUE)
    private String image;

    @Column(name = "weight")
    private BigDecimal weight;

    @Column(name = "length")
    private Integer length;

    @Column(name = "width")
    private Integer width;

    @Column(name = "height")
    private Integer height;

    @OneToMany(mappedBy = "inventoryid")
    private Set<Cartitem> cartitems = new LinkedHashSet<>();

    @OneToMany(mappedBy = "inventoryid")
    private Set<Orderitem> orderitems = new LinkedHashSet<>();

}