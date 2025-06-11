package org.example.ecommerce.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.Nationalized;

@Entity
@Table(name = "inventory")
public class Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inventoryid", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "productid")
    private Product productid;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "lowstockthreshold")
    private Integer lowstockthreshold;

    @Column(name = "solditems")
    private Integer solditems;

    @Size(max = 100)
    @Nationalized
    @Column(name = "color", length = 100)
    private String color;

    @Size(max = 100)
    @Nationalized
    @Column(name = "dimension", length = 100)
    private String dimension;

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

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getLowstockthreshold() {
        return lowstockthreshold;
    }

    public void setLowstockthreshold(Integer lowstockthreshold) {
        this.lowstockthreshold = lowstockthreshold;
    }

    public Integer getSolditems() {
        return solditems;
    }

    public void setSolditems(Integer solditems) {
        this.solditems = solditems;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getDimension() {
        return dimension;
    }

    public void setDimension(String dimension) {
        this.dimension = dimension;
    }

}