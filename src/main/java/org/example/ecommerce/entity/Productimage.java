package org.example.ecommerce.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "productimages")
@Data
public class Productimage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "imageid", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "productid")
    private Product productid;

    @NotNull
    @Column(name = "imageurl", nullable = false, length = Integer.MAX_VALUE)
    private String imageurl;

    @Column(
            name = "embedding",
            columnDefinition = "vector(512)"
    )
    @org.hibernate.annotations.JdbcTypeCode(java.sql.Types.ARRAY)
    private Float[] embedding;


}