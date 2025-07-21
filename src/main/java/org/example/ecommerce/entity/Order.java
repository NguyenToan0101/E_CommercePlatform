package org.example.ecommerce.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.example.ecommerce.entity.conplaint.Complaint;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "orderid", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customerid")
    private Customer customerid;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "orderdate")
    private Instant orderdate;

    @NotNull
    @Column(name = "totalamount", nullable = false)
    private BigDecimal totalamount;

    @Column(name = "status", length = Integer.MAX_VALUE)
    private String status;

    @Column(name = "payment_status", length = Integer.MAX_VALUE)
    private String paymentStatus;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @Column(name = "fullname", length = Integer.MAX_VALUE)
    private String fullname;

    @Column(name = "phone", length = Integer.MAX_VALUE)
    private String phone;

    @Column(name = "address", length = Integer.MAX_VALUE)
    private String address;

    @OneToMany(mappedBy = "orderId")
    private Set<Complaint> complaints = new LinkedHashSet<>();

    @OneToMany(mappedBy = "orderid")
    private Set<Orderitem> orderitems = new LinkedHashSet<>();

    @OneToMany(mappedBy = "orderid")
    private Set<Ordernotification> ordernotifications = new LinkedHashSet<>();

    @OneToMany(mappedBy = "orderid")
    private Set<Payment> payments = new LinkedHashSet<>();

    public String getStatusClass() {
        if (status == null) return "";
        switch (status) {
            case "Chờ xác nhận": return "seller-status-cho_xac_nhan";
            case "Đã xác nhận": return "seller-status-da_xac_nhan";
            case "Chờ lấy hàng": return "seller-status-cho_lay_hang";
            case "Đã hủy": return "seller-status-da_huy";
            case "Yêu cầu trả hàng/hoàn tiền": return "seller-status-yeu_cau_tra_hang_hoan_tien";
            case "Đang giao": return "seller-status-dang_giao";
            case "Đã giao": return "seller-status-da_giao";
            default: return "";
        }
    }
}