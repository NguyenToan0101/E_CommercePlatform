package org.example.ecommerce.entity;

import jakarta.persistence.*;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.annotations.Nationalized;


@Entity
@Table(name = "sellers")
@Data
public class Seller {
    @Id
    @Column(name = "sellerid", nullable = false)
    private Integer id;
    @OneToOne(cascade = CascadeType.ALL,mappedBy = "sellerid")

    private Shop shop;
    @OneToOne()
    @JoinColumn(name = "sellerid")
    @MapsId
    private Customer customer;
    @Version
    private Integer version;
    @Size(max = 20)
    @Nationalized
    @Column(name = "idnumber", nullable = false, length = 20)
    private String idNumber;
    @Column(name = "frontidimage")
    private String frontIdImage; // base64 hoặc URL
    @Column(name = "backidimage")
    private String backIdImage;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sellerid", nullable = false)
    private Customer customer1;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sellerid", nullable = false)
    private Customer customer2;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sellerid", nullable = false)
    private Customer customer3;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sellerid", nullable = false)
    private Customer customer4;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sellerid", nullable = false)
    private Customer customer5;


    public Seller() {
    }
    public Seller(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getFrontIdImage() {
        return frontIdImage;
    }

    public void setFrontIdImage(String frontIdImage) {
        this.frontIdImage = frontIdImage;
    }

    public String getBackIdImage() {
        return backIdImage;
    }

    public void setBackIdImage(String backIdImage) {
        this.backIdImage = backIdImage;
    }
}