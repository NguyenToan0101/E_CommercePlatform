package org.example.ecommerce.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "order_fact_log")
public class OrderFactLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ColumnDefault("nextval('order_fact_log_id_seq'")
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @Column(name = "order_id", nullable = false)
    private Integer orderId;

    @NotNull
    @Column(name = "order_item_id", nullable = false)
    private Integer orderItemId;

    @Column(name = "customer_id")
    private Integer customerId;

    @Column(name = "product_id")
    private Integer productId;

    @Column(name = "category_id")
    private Integer categoryId;

    @Column(name = "inventory_id")
    private Integer inventoryId;

    @Column(name = "promotion_id")
    private Integer promotionId;

    @Column(name = "shop_id")
    private Integer shopId;

    @Column(name = "region", length = Integer.MAX_VALUE)
    private String region;

    @Column(name = "province", length = Integer.MAX_VALUE)
    private String province;



    @NotNull
    @Column(name = "order_date", nullable = false)
    private Instant orderDate;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "unit_price", precision = 18, scale = 2)
    private BigDecimal unitPrice;

    @Column(name = "discount_amount", precision = 18, scale = 2)
    private BigDecimal discountAmount;

    @Column(name = "total_amount", precision = 18, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "cost", precision = 18, scale = 2)
    private BigDecimal cost;

    @Column(name = "profit", precision = 18, scale = 2)
    private BigDecimal profit;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at")
    private Instant createdAt;



}