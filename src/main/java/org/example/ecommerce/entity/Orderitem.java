package org.example.ecommerce.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;


@Entity
@Table(name = "orderitems")
public class Orderitem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "orderitemid", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orderid")
    private Order orderid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "productid")
    private Product productid;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "unitprice")
    private BigDecimal unitprice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inventoryid")
    private Inventory inventoryid;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Order getOrderid() {
        return orderid;
    }

    public void setOrderid(Order orderid) {
        this.orderid = orderid;
    }

    public Product getProductid() {
        return productid;
    }

    public void setProductid(Product productid) {
        this.productid = productid;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitprice() {
        return unitprice;
    }

    public void setUnitprice(BigDecimal unitprice) {
        this.unitprice = unitprice;
    }

    public Inventory getInventoryid() {
        return inventoryid;
    }

    public void setInventoryid(Inventory inventoryid) {
        this.inventoryid = inventoryid;
    }
}