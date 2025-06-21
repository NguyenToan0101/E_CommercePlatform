package org.example.ecommerce.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "cartitems")
public class Cartitem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cartitemid", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inventoryid")
    private Inventory inventoryid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cartid")
    private Cart cartid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "productid")
    private Product productid;

    @Column(name = "quantity")
    private Integer quantity;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Inventory getInventoryid() {
        return inventoryid;
    }

    public void setInventoryid(Inventory inventoryid) {
        this.inventoryid = inventoryid;
    }

    public Cart getCartid() {
        return cartid;
    }

    public void setCartid(Cart cartid) {
        this.cartid = cartid;
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

}