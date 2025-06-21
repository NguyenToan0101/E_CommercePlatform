package org.example.ecommerce.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.Nationalized;

import java.time.Instant;

@Entity
@Table(name = "reviews")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reviewid", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "productid")
    private Product productid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customerid")
    private Customer customerid;

    @Column(name = "rating")
    private Integer rating;

    @Nationalized
    @Lob
    @Column(name = "comment")
    private String comment;

    @Size(max = 255)
    @Nationalized
    @Column(name = "imagereviews")
    private String imagereviews;

    @Column(name = "createdat")
    private Instant createdat;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Product getProductid() {
        return productid;
    }

    public void setProductid(Product productid) {
        this.productid = productid;
    }

    public Customer getCustomerid() {
        return customerid;
    }

    public void setCustomerid(Customer customerid) {
        this.customerid = customerid;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getImagereviews() {
        return imagereviews;
    }

    public void setImagereviews(String imagereviews) {
        this.imagereviews = imagereviews;
    }

    public Instant getCreatedat() {
        return createdat;
    }

    public void setCreatedat(Instant createdat) {
        this.createdat = createdat;
    }

}