package org.example.ecommerce.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "promotions")
public class Promotion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "promotionid", nullable = false)
    private Integer id;

    @NotNull
    @Column(name = "code", nullable = false, length = Integer.MAX_VALUE)
    private String code;

    @Column(name = "description", length = Integer.MAX_VALUE)
    private String description;

    @Column(name = "discountpercent")
    private BigDecimal discountpercent;

    @Column(name = "discountamount")
    private BigDecimal discountamount;

    @NotNull
    @Column(name = "startdate", nullable = false)
    private LocalDate startdate;

    @NotNull
    @Column(name = "enddate", nullable = false)
    private LocalDate enddate;

    @ColumnDefault("false")
    @Column(name = "isglobal")
    private Boolean isglobal;

    @Column(name = "createdby")
    private Integer createdby;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sellerid")
    private Seller sellerid;

    @OneToMany(mappedBy = "promotionid")
    private Set<PromotionTarget> promotionTargets = new LinkedHashSet<>();

    @OneToMany(mappedBy = "promotionid")
    private Set<Userpromotion> userpromotions = new LinkedHashSet<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getDiscountpercent() {
        return discountpercent;
    }

    public void setDiscountpercent(BigDecimal discountpercent) {
        this.discountpercent = discountpercent;
    }

    public BigDecimal getDiscountamount() {
        return discountamount;
    }

    public void setDiscountamount(BigDecimal discountamount) {
        this.discountamount = discountamount;
    }

    public LocalDate getStartdate() {
        return startdate;
    }

    public void setStartdate(LocalDate startdate) {
        this.startdate = startdate;
    }

    public LocalDate getEnddate() {
        return enddate;
    }

    public void setEnddate(LocalDate enddate) {
        this.enddate = enddate;
    }

    public Boolean getIsglobal() {
        return isglobal;
    }

    public void setIsglobal(Boolean isglobal) {
        this.isglobal = isglobal;
    }

    public Integer getCreatedby() {
        return createdby;
    }

    public void setCreatedby(Integer createdby) {
        this.createdby = createdby;
    }

    public Seller getSellerid() {
        return sellerid;
    }

    public void setSellerid(Seller sellerid) {
        this.sellerid = sellerid;
    }

    public Set<PromotionTarget> getPromotionTargets() {
        return promotionTargets;
    }

    public void setPromotionTargets(Set<PromotionTarget> promotionTargets) {
        this.promotionTargets = promotionTargets;
    }

    public Set<Userpromotion> getUserpromotions() {
        return userpromotions;
    }

    public void setUserpromotions(Set<Userpromotion> userpromotions) {
        this.userpromotions = userpromotions;
    }

}