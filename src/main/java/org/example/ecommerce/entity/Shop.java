package org.example.ecommerce.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.hibernate.annotations.Nationalized;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Objects;

@Entity
@Table(name = "shop")
@Getter
@Setter
@ToString(exclude = "sellerid")
public class Shop {

    @Id
    @Column(name = "shopid", nullable = false)
    private Integer id;

    @OneToOne()
    @JoinColumn(name = "shopid", nullable = false, unique = true)
    @MapsId
    @JsonIgnore
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
    private Boolean locked = false;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Shop shop = (Shop) o;
        return Objects.equals(id, shop.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
