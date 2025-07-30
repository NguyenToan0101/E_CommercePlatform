package org.example.ecommerce.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.hibernate.annotations.Nationalized;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "shop")
@Data
public class Shop {

    @Id
    @Column(name = "shopid", nullable = false)
    private Integer id;

    @OneToOne()
    @JoinColumn(name = "shopid", nullable = false, unique = true)
    @MapsId
    private Seller sellerid;

    @Version
    private Integer version;

    @Nationalized
    @Column(name = "shopname", nullable = false, unique = true, length = 100)
    private String shopname;

    @Size(max = 500)
    @Nationalized
    @Column(name = "description", length = 500)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maincategoryid")
    private Category maincategoryid;

    @Nationalized
    @Column(name = "fulladdress", nullable = false, unique = true)
    private String fulladdress;

    @Column(name = "createdat", nullable = false, columnDefinition = "timestamp default CURRENT_TIMESTAMP")
    private LocalDateTime createdat = LocalDateTime.now();


    @Column(name = "status")
    private String status;


    @Nationalized
    @Column(name = "imageshop")
    private String imageshop;


    @Nationalized
    @Column(name = "managename", nullable = false)
    private String manageName;

    @Column(name = "phone", nullable = false, unique = true)
    private String phone;

    @Column(name = "express")
    private Boolean express;

    @Column(name = "fast")
    private Boolean fast;

    @Column(name = "economy")
    private Boolean economy;

    @Column(name = "lockerdelivery")
    private Boolean lockerDelivery;

    @Column(name = "bulkyitems")
    private Boolean bulkyItems;


    @Nationalized
    @Column(name = "businesstype", nullable = false, unique = true)
    private String businessType;

    @Size(max = 255)
    @Nationalized
    @Column(name = "businessaddress", nullable = false, unique = true)
    private String businessAddress;

    @Email
    @Size(max = 100)
    @Column(name = "invoiceemail", nullable = false, unique = true)
    private String invoiceEmail;

    @Size(max = 20)
    @Column(name = "taxcode", nullable = false, unique = true)
    private String taxCode;

    // New fields added for lock functionality
    @Column(name = "locked", nullable = false)
    private Boolean locked = false; // default to false

    @Column(name = "locked_until")
    private LocalDateTime lockedUntil;

    public Shop(Integer id, String status) {
        this.id = id;
        this.status = status;
    }

    public Shop() {
    }

    public enum Status {
        PENDING_APPROVAL,
        ACTIVE,
        LOCK,
        REJECTED
    }

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

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getShopname() {
        return shopname;
    }

    public void setShopname(String shopname) {
        this.shopname = shopname;
    }

    public @Size(max = 500) String getDescription() {
        return description;
    }

    public void setDescription(@Size(max = 500) String description) {
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

    public LocalDateTime getCreatedat() {
        return createdat;
    }

    public void setCreatedat(LocalDateTime createdat) {
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

    public String getManageName() {
        return manageName;
    }

    public void setManageName(String manageName) {
        this.manageName = manageName;
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

    public Boolean getLockerDelivery() {
        return lockerDelivery;
    }

    public void setLockerDelivery(Boolean lockerDelivery) {
        this.lockerDelivery = lockerDelivery;
    }

    public Boolean getBulkyItems() {
        return bulkyItems;
    }

    public void setBulkyItems(Boolean bulkyItems) {
        this.bulkyItems = bulkyItems;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public @Size(max = 255) String getBusinessAddress() {
        return businessAddress;
    }

    public void setBusinessAddress(@Size(max = 255) String businessAddress) {
        this.businessAddress = businessAddress;
    }

    public @Email @Size(max = 100) String getInvoiceEmail() {
        return invoiceEmail;
    }

    public void setInvoiceEmail(@Email @Size(max = 100) String invoiceEmail) {
        this.invoiceEmail = invoiceEmail;
    }

    public @Size(max = 20) String getTaxCode() {
        return taxCode;
    }

    public void setTaxCode(@Size(max = 20) String taxCode) {
        this.taxCode = taxCode;
    }

    public Boolean getLocked() {
        return locked;
    }

    public void setLocked(Boolean locked) {
        this.locked = locked;
    }

    public LocalDateTime getLockedUntil() {
        return lockedUntil;
    }

    public void setLockedUntil(LocalDateTime lockedUntil) {
        this.lockedUntil = lockedUntil;
    }
}
