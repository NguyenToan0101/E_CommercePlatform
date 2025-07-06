package org.example.ecommerce.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
    private Category parent;

    @OneToMany(mappedBy = "parent")
    private Set<Category> children = new LinkedHashSet<>();

    @OneToMany(mappedBy = "categoryid")
    private Set<Product> products = new LinkedHashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "categoryid", fetch = FetchType.LAZY)
    private Set<PromotionTarget> promotionTargets = new LinkedHashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "maincategoryid", fetch = FetchType.LAZY)
    private Set<Shop> shops = new LinkedHashSet<>();

}