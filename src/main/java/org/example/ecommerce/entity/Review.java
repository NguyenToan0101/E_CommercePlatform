package org.example.ecommerce.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "reviews")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ColumnDefault("nextval('reviews_reviewid_seq'")
    @Column(name = "reviewid", nullable = false)
    private Integer id;

    @Column(name = "productid")
    private Integer productid;

    @Column(name = "orderitemsid")
    private Integer orderitemid;

    @Column(name = "rating")
    private Integer rating;

    @Column(name = "comment", length = Integer.MAX_VALUE)
    private String comment;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "createdat")
    private Instant createdat;

    @ColumnDefault("false")
    @Column(name = "is_hidden")
    private Boolean isHidden;
}