package org.example.ecommerce.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "payments")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "paymentid", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "walletid", nullable = false)
    private Wallet walletid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orderid")
    private Order orderid;

    @NotNull
    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "method", length = Integer.MAX_VALUE)
    private String method;

    @Column(name = "paymentstatus", length = Integer.MAX_VALUE)
    private String paymentstatus;

    @Column(name = "transaction_id", length = Integer.MAX_VALUE)
    private String transactionId;

    @Column(name = "gateway", length = Integer.MAX_VALUE)
    private String gateway;

    @Column(name = "paidat")
    private Instant paidat;

}