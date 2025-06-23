package org.example.ecommerce.entity;

import jakarta.persistence.*;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "sellers")
public class Seller {
    @Id
    @Column(name = "sellerid", nullable = false)
    private Integer id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sellerid", nullable = false)
    private Customer customer;

    @Column(name = "idnumber", length = Integer.MAX_VALUE)
    private String idnumber;

    @Column(name = "frontidimage", length = Integer.MAX_VALUE)
    private String frontidimage;

    @Column(name = "backidimage", length = Integer.MAX_VALUE)
    private String backidimage;

    @Column(name = "version")
    private Long version;

    @OneToMany(mappedBy = "sellerid")
    private Set<Conversation> conversations = new LinkedHashSet<>();

    @OneToMany(mappedBy = "sellerid")
    private Set<Promotion> promotions = new LinkedHashSet<>();

    @OneToOne
    private Shop shop;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getIdnumber() {
        return idnumber;
    }

    public void setIdnumber(String idnumber) {
        this.idnumber = idnumber;
    }

    public String getFrontidimage() {
        return frontidimage;
    }

    public void setFrontidimage(String frontidimage) {
        this.frontidimage = frontidimage;
    }

    public String getBackidimage() {
        return backidimage;
    }

    public void setBackidimage(String backidimage) {
        this.backidimage = backidimage;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public Set<Conversation> getConversations() {
        return conversations;
    }

    public void setConversations(Set<Conversation> conversations) {
        this.conversations = conversations;
    }

    public Set<Promotion> getPromotions() {
        return promotions;
    }

    public void setPromotions(Set<Promotion> promotions) {
        this.promotions = promotions;
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

}