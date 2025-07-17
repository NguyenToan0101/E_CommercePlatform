package org.example.ecommerce.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "inventory")
@Getter
 @Setter
public class Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inventoryid", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "productid", nullable = false)
    private Product productid;

    @NotNull(message = "Số lượng không được để trống")
    @Min(value = 0, message = "Số lượng không được âm")
    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "solditems")
    private Integer solditems;

    @Size(max = 100)
    @NotEmpty(message = "Màu sắc không được để trống")
    @Column(name = "color", nullable = false, length = 100)
    private String color;

    @Size(max = 100)
    @NotEmpty(message = "Kích thước không được để trống")
    @Column(name = "dimension", nullable = false, length = 100)
    private String dimension;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @NotNull(message = "Giá không được để trống")
    @DecimalMin(value = "0.0", inclusive = false, message = "Giá phải lớn hơn 0")
    @Column(name = "price")
    private BigDecimal price;




    @Column(name = "image")
    // @NotEmpty(message = "Ảnh không được để trống") // Đã bỏ validate này để tránh lỗi khi upload file
    private String image;

    @Column(name = "weight")
    private Integer weight;

    @Column(name = "length")
    private Integer length;

    @Column(name = "width")
    private Integer width;

    @Column(name = "height")
    private Integer height;

    @OneToMany(mappedBy = "inventoryid")
    private Set<Cartitem> cartitems = new LinkedHashSet<>();

    @OneToMany(mappedBy = "inventoryid")
    private Set<Orderitem> orderitems = new LinkedHashSet<>();

}