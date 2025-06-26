package org.example.ecommerce.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;


@Entity
@Table(name = "shop")
public class Shop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shopid", nullable = false)
    private Integer id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "shopid", nullable = false)
    private Seller sellers;

    @Column(name = "shopname", length = Integer.MAX_VALUE)
    private String shopname;

    @Column(name = "description", length = Integer.MAX_VALUE)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maincategoryid")
    private Category maincategoryid;

    @Column(name = "fulladdress", length = Integer.MAX_VALUE)
    private String fulladdress;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "createdat")
    private Instant createdat;

    @Column(name = "status", length = Integer.MAX_VALUE)
    private String status;

    @Column(name = "imageshop", length = Integer.MAX_VALUE)
    private String imageshop;

    @Column(name = "managename", length = Integer.MAX_VALUE)
    private String managename;

    @Column(name = "phone", length = Integer.MAX_VALUE)
    private String phone;

    @Column(name = "express")
    private Boolean express;

    @Column(name = "fast")
    private Boolean fast;

    @Column(name = "economy")
    private Boolean economy;

    @Column(name = "lockerdelivery")
    private Boolean lockerdelivery;

    @Column(name = "bulkyitems")
    private Boolean bulkyitems;

    @Column(name = "businesstype", length = Integer.MAX_VALUE)
    private String businesstype;

    @Column(name = "businessaddress", length = Integer.MAX_VALUE)
    private String businessaddress;

    @Column(name = "invoiceemail", length = Integer.MAX_VALUE)
    private String invoiceemail;

    @Column(name = "taxcode", length = Integer.MAX_VALUE)
    private String taxcode;

    @Column(name = "version")
    private Long version;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Seller getSellers() {
        return sellers;
    }

    public void setSellers(Seller sellers) {
        this.sellers = sellers;
    }

    public String getShopname() {
        return shopname;
    }

    public void setShopname(String shopname) {
        this.shopname = shopname;
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

    public String getFulladdress() {
        return fulladdress;
    }

    public void setFulladdress(String fulladdress) {
        this.fulladdress = fulladdress;
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

    public String getImageshop() {
        return imageshop;
    }

    public void setImageshop(String imageshop) {
        this.imageshop = imageshop;
    }

    public String getManagename() {
        return managename;
    }

    public void setManagename(String managename) {
        this.managename = managename;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Boolean getExpress() {
        return express;
    }

    public void setExpress(Boolean express) {
        this.express = express;
    }

    public Boolean getFast() {
        return fast;
    }

    public void setFast(Boolean fast) {
        this.fast = fast;
    }

    public Boolean getEconomy() {
        return economy;
    }

    public void setEconomy(Boolean economy) {
        this.economy = economy;
    }

    public Boolean getLockerdelivery() {
        return lockerdelivery;
    }

    public void setLockerdelivery(Boolean lockerdelivery) {
        this.lockerdelivery = lockerdelivery;
    }

    public Boolean getBulkyitems() {
        return bulkyitems;
    }

    public void setBulkyitems(Boolean bulkyitems) {
        this.bulkyitems = bulkyitems;
    }

    public String getBusinesstype() {
        return businesstype;
    }

    public void setBusinesstype(String businesstype) {
        this.businesstype = businesstype;
    }

    public String getBusinessaddress() {
        return businessaddress;
    }

    public void setBusinessaddress(String businessaddress) {
        this.businessaddress = businessaddress;
    }

    public String getInvoiceemail() {
        return invoiceemail;
    }

    public void setInvoiceemail(String invoiceemail) {
        this.invoiceemail = invoiceemail;
    }

    public String getTaxcode() {
        return taxcode;
    }

    public void setTaxcode(String taxcode) {
        this.taxcode = taxcode;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
}