package org.example.ecommerce.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Nationalized;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "categories")
@ToString(exclude = {"parent", "children", "promotions"})

public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "categoryid", nullable = false)
    private Integer id;

    @Nationalized
    @Column(name = "categoryname", length = 100)
    private String categoryname;

    @Column(name = "image", length = Integer.MAX_VALUE)
    private String image;

    @ManyToOne()
    @JoinColumn(name = "parentid")
    @JsonIgnore
    private Category parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("parent")
    private List<Category> children ;
    @ManyToMany(mappedBy = "categories")
    private List<Promotion> promotions;

    public Category() {}

    @OneToMany(mappedBy = "categoryid")
    private Set<PromotionTarget> promotionTargets = new LinkedHashSet<>();

    public Category(Integer id, String categoryname) {
        this.id = id;
        this.categoryname = categoryname;
    }
}