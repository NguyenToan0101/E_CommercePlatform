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

    public Shop(Integer id,String status) {
        this.id = id;
        this.status = status;
    }

    public Shop() {

    }

    public enum Status{
        PENDING_APPROVAL,
        ACTIVE,
        LOCK
    }
}
