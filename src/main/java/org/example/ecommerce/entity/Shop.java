package org.example.ecommerce.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.Nationalized;

import java.time.Instant;

@Entity
@Table(name = "shops")
public class Shop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shopid", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sellerid")
    private Seller sellerid;

    @Size(max = 100)
    @Nationalized
    @Column(name = "shopname", length = 100)
    private String shopname;

    @Size(max = 255)
    @Nationalized
    @Column(name = "imageshop")
    private String imageshop;

    @Size(max = 500)
    @Nationalized
    @Column(name = "description", length = 500)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maincategoryid")
    private Category maincategoryid;

    @Size(max = 255)
    @Nationalized
    @Column(name = "warehouseaddress")
    private String warehouseaddress;

    @Column(name = "createdat")
    private Instant createdat;

    @Size(max = 20)
    @Column(name = "status", length = 20)
    private String status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Seller getSellerid() {
        return sellerid;
    }

    public void setSellerid(Seller sellerid) {
        this.sellerid = sellerid;
    }

    public String getShopname() {
        return shopname;
    }

    public void setShopname(String shopname) {
        this.shopname = shopname;
    }

    public String getImageshop() {
        return imageshop;
    }

    public void setImageshop(String imageshop) {
        this.imageshop = imageshop;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Category getMaincategoryid() {
        return maincategoryid;
    }

    public void setMaincategoryid(Category maincategoryid) {
        this.maincategoryid = maincategoryid;
    }

    public String getWarehouseaddress() {
        return warehouseaddress;
    }

    public void setWarehouseaddress(String warehouseaddress) {
        this.warehouseaddress = warehouseaddress;
    }

    public Instant getCreatedat() {
        return createdat;
    }

    public void setCreatedat(Instant createdat) {
        this.createdat = createdat;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}