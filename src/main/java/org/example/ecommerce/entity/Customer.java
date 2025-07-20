package org.example.ecommerce.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.example.ecommerce.entity.conplaint.Complaint;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "customer")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customerid", nullable = false)
    private Integer id;

    @Column(name = "firstname", length = Integer.MAX_VALUE)
    private String firstname;

    @Column(name = "lastname", length = Integer.MAX_VALUE)
    private String lastname;

    @NotNull
    @Column(name = "email", nullable = false, length = Integer.MAX_VALUE)
    private String email;

    @NotNull
    @Column(name = "phone", nullable = false, length = Integer.MAX_VALUE)
    private String phone;

    @Column(name = "gender", length = Integer.MAX_VALUE)
    private String gender;

    @Column(name = "dateofbirth")
    private LocalDate dateofbirth;

    @Column(name = "address", length = Integer.MAX_VALUE)
    private String address;

    @Column(name = "password", length = Integer.MAX_VALUE)
    private String password;

    @Column(name = "role", length = Integer.MAX_VALUE)
    private String role;

    @Column(name = "status" ,nullable = false)
    private Boolean status;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "createdat")
    private Instant createdat;

    @Column(name = "image")
    private String image;

    @Column(name = "version")
    private Integer version;

    @OneToMany(mappedBy = "customerid")
    private Set<Cart> carts = new LinkedHashSet<>();

    @OneToMany(mappedBy = "customer")
    private Set<Complaint> complaints = new LinkedHashSet<>();

    @OneToMany(mappedBy = "customerid")
    private Set<Conversation> conversations = new LinkedHashSet<>();

    @Column(name = "locked_until")
    private LocalDateTime lockedUntil;

    @Column(name = "is_locked")
    private boolean isLocked;

    @OneToMany(mappedBy = "customerid")
    private Set<Ordernotification> ordernotifications = new LinkedHashSet<>();

    @OneToMany(mappedBy = "customerid")  // mappedBy trùng với tên biến Customer trong Order
    private Set<Order> orders = new LinkedHashSet<>();


    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL,mappedBy = "customer")
    private Seller seller;

    @OneToOne(mappedBy = "customerid")
    private Wallet wallet;

    @OneToMany(mappedBy = "customerid")
    private Set<Wishlist> wishlists = new LinkedHashSet<>();

   }