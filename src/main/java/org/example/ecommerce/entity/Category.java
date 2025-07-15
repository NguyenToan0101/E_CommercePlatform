

package org.example.ecommerce.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Nationalized;



import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "categories")
@Getter
@Setter
@ToString(exclude = {"parent", "children", "promotions"})
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "categoryid", nullable = false)
    private Integer id;

    @Nationalized
    @Column(name = "categoryname", length = 100)
    private String categoryname;

    @Nationalized
    @Column(name = "image")
    private String image;

    @ManyToOne()
    @JoinColumn(name = "parentid")
    @JsonIgnore
    private Category parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("parent")
    private List<Category> children ;

    private String status;
    private LocalDateTime create_at;
    @ManyToMany(mappedBy = "categories")
    private List<Promotion> promotions;

    public Category() {

    }

    public Category(Integer id, String categoryname) {
        this.id = id;
        this.categoryname = categoryname;
    }
    public enum Status{
        ACTIVE,
        INACTIVE
    }
}