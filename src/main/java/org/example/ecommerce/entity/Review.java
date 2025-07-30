package org.example.ecommerce.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.ecommerce.entity.conplaint.Complaint;
import org.hibernate.annotations.ColumnDefault;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "productid")
    private Product productid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orderitemsid")
    private Orderitem orderitemsid;

    @Column(name = "rating")
    private Integer rating;

    @Column(name = "comment", length = Integer.MAX_VALUE)
    private String comment;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "createdat")
    private LocalDateTime createdat;

    @ColumnDefault("false")
    @Column(name = "is_hidden")
    private Boolean isHidden;

    @ManyToMany
    private Set<Complaint> complaints = new LinkedHashSet<>();

    @OneToMany(mappedBy = "review")
    private Set<ReviewReply> reviewReplies = new LinkedHashSet<>();

    @OneToMany(mappedBy = "reviewid")
    private Set<ReviewsImage> reviewsImages = new LinkedHashSet<>();

}