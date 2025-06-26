package org.example.ecommerce.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;
import java.time.LocalDate;
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

    @Column(name = "status", length = Integer.MAX_VALUE)
    private String status;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "createdat")
    private Instant createdat;

    @Column(name = "image", length = Integer.MAX_VALUE)
    private byte[] image;

    @Column(name = "version")
    private Long version;

    @OneToMany(mappedBy = "customerid")
    private Set<Cart> carts = new LinkedHashSet<>();

    @OneToMany(mappedBy = "customerid")
    private Set<Complaint> complaints = new LinkedHashSet<>();

    @OneToMany(mappedBy = "customerid")
    private Set<Conversation> conversations = new LinkedHashSet<>();

    @OneToMany(mappedBy = "customerid")
    private Set<Ordernotification> ordernotifications = new LinkedHashSet<>();

    @OneToMany(mappedBy = "customerid")
    private Set<Order> orders = new LinkedHashSet<>();

    @OneToMany(mappedBy = "customerid")
    private Set<Review> reviews = new LinkedHashSet<>();

    @OneToOne(mappedBy = "customer", cascade = CascadeType.ALL)
    private Seller seller;

    @OneToOne(mappedBy = "customerid")
    private Wallet wallet;

    @OneToMany(mappedBy = "customerid")
    private Set<Wishlist> wishlists = new LinkedHashSet<>();

}