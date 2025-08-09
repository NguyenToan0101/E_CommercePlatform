package org.example.ecommerce.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.example.ecommerce.entity.conplaint.Complaint;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;


@Entity
@Getter
@Setter
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
    private LocalDateTime orderdate;

    @NotNull
    @Column(name = "totalamount", nullable = false)
    private BigDecimal totalamount;

    @Column(name = "status", length = Integer.MAX_VALUE)
    private String status;

    @Column(name = "payment_status", length = Integer.MAX_VALUE)
    private String paymentStatus;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "fullname", length = Integer.MAX_VALUE)
    private String fullname;

    @Column(name = "phone", length = Integer.MAX_VALUE)
    private String phone;

    @Column(name = "address", length = Integer.MAX_VALUE)
    private String address;


    @OneToMany(mappedBy = "orderid")
    private Set<Orderitem> orderitems = new LinkedHashSet<>();

    public void setId(Integer id) {
        this.id = id;
    }

    public Customer getCustomerid() {
        return customerid;
    }

    public void setCustomerid(Customer customerid) {
        this.customerid = customerid;
    }

    public LocalDateTime getOrderdate() {
        return orderdate;
    }

    public void setOrderdate(LocalDateTime orderdate) {
        this.orderdate = orderdate;
    }

    public BigDecimal getTotalamount() {
        return totalamount;
    }

    public void setTotalamount(BigDecimal totalamount) {
        this.totalamount = totalamount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getPhone() {
        return phone;
    }



    public String getStatusClass() {
        if (status == null) return "";
        switch (status) {
            case "Chờ xác nhận":
                return "seller-status-cho_xac_nhan";
            case "Đã xác nhận":
                return "seller-status-da_xac_nhan";
            case "Chờ lấy hàng":
                return "seller-status-cho_lay_hang";
            case "Đã hủy":
                return "seller-status-da_huy";
            case "Yêu cầu trả hàng/hoàn tiền":
                return "seller-status-yeu_cau_tra_hang_hoan_tien";
            case "Đang giao":
                return "seller-status-dang_giao";
            case "Đã giao":
                return "seller-status-da_giao";
            case "Đã hoàn tiền":
                return "seller-status-da_hoan_tien";
            default:
                return "";
        }
    }


    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}