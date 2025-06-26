package org.example.ecommerce.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;

import java.time.Instant;

@Entity
@Table(name = "payments")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "paymentid", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "walletid")
    private Wallet walletid;

    @Size(max = 50)
    @Column(name = "method", length = 50)
    private String method;

    @Size(max = 20)
    @Column(name = "paymentstatus", length = 20)
    private String paymentstatus;

    @Column(name = "paidat")
    private Instant paidat;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orderid")
    private Order orderid;

    public Order getOrderid() {
        return orderid;
    }

    public void setOrderid(Order orderid) {
        this.orderid = orderid;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Wallet getWalletid() {
        return walletid;
    }

    public void setWalletid(Wallet walletid) {
        this.walletid = walletid;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getPaymentstatus() {
        return paymentstatus;
    }

    public void setPaymentstatus(String paymentstatus) {
        this.paymentstatus = paymentstatus;
    }

    public Instant getPaidat() {
        return paidat;
    }

    public void setPaidat(Instant paidat) {
        this.paidat = paidat;
    }

}