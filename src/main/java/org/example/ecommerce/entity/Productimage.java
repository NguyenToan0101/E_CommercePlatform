package org.example.ecommerce.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Objects;

@Entity
@Table(name = "productimages")
@Getter
@Setter
@ToString(exclude = "productid")
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Productimage that = (Productimage) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}