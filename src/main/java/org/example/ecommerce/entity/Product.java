package org.example.ecommerce.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.annotations.Nationalized;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

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

    @Column(name = "createdat")
    private Instant createdat;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoryid")
    private Category categoryid;

    @OneToMany(mappedBy = "productid",cascade = CascadeType.ALL)
    private List<PromotionTarget> promotionTargets = new ArrayList<>();



}