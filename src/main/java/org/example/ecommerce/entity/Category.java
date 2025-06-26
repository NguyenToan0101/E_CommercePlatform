package org.example.ecommerce.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
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

    @Column(name = "image", length = Integer.MAX_VALUE)
    private String image;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parentid")
    private Category parentid;

    @OneToMany(mappedBy = "parentid")
    private Set<Category> categories = new LinkedHashSet<>();

    @OneToMany(mappedBy = "categoryid")
    private Set<Product> products = new LinkedHashSet<>();

    @OneToMany(mappedBy = "categoryid")
    private Set<PromotionTarget> promotionTargets = new LinkedHashSet<>();

    @OneToMany(mappedBy = "maincategoryid")
    private Set<Shop> shops = new LinkedHashSet<>();

}