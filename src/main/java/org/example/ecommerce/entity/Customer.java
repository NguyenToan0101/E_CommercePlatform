package org.example.ecommerce.entity;

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


    @Column(nullable = false)
    private boolean status;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "createdat")
    private Instant createdat;

    @Column(name = "image")
    private byte[] image;

    @Column(name = "version")
    private Integer version;

    @OneToMany(mappedBy = "customerid")
    private Set<Cart> carts = new LinkedHashSet<>();

    @OneToMany(mappedBy = "customerid")
    private Set<Complaint> complaints = new LinkedHashSet<>();

    @OneToMany(mappedBy = "customerid")
    private Set<Conversation> conversations = new LinkedHashSet<>();

    @Column(name = "locked_until")
    private LocalDateTime lockedUntil;

    @Column(name = "is_locked")
    private boolean isLocked;

    @OneToMany(mappedBy = "customerid")
    private Set<Ordernotification> ordernotifications = new LinkedHashSet<>();

    @OneToMany(mappedBy = "customerid")
    private Set<Order> orders = new LinkedHashSet<>();

    @OneToOne(cascade = CascadeType.ALL,mappedBy = "customer")
    private Seller seller;

    @OneToOne(mappedBy = "customerid")
    private Wallet wallet;

    @OneToMany(mappedBy = "customerid")
    private Set<Wishlist> wishlists = new LinkedHashSet<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public LocalDate getDateofbirth() {
        return dateofbirth;
    }

    public void setDateofbirth(LocalDate dateofbirth) {
        this.dateofbirth = dateofbirth;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public LocalDateTime getLockedUntil() {
        return lockedUntil;
    }

    public void setLockedUntil(LocalDateTime lockedUntil) {
        this.lockedUntil = lockedUntil;
    }

    public boolean isLocked() {
        return isLocked;
    }

    public void setLocked(boolean locked) {
        isLocked = locked;
    }

    public Instant getCreatedat() {
        return createdat;
    }

    public void setCreatedat(Instant createdat) {
        this.createdat = createdat;
    }



    public Set<Cart> getCarts() {
        return carts;
    }

    public void setCarts(Set<Cart> carts) {
        this.carts = carts;
    }


    public Set<Conversation> getConversations() {
        return conversations;
    }

    public void setConversations(Set<Conversation> conversations) {
        this.conversations = conversations;
    }

    public Set<Ordernotification> getOrdernotifications() {
        return ordernotifications;
    }

    public void setOrdernotifications(Set<Ordernotification> ordernotifications) {
        this.ordernotifications = ordernotifications;
    }

    public Set<Order> getOrders() {
        return orders;
    }

    public void setOrders(Set<Order> orders) {
        this.orders = orders;
    }

    public Seller getSeller() {
        return seller;
    }

    public void setSeller(Seller seller) {
        this.seller = seller;
    }

    public Wallet getWallet() {
        return wallet;
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }

    public Set<Wishlist> getWishlists() {
        return wishlists;
    }

    public void setWishlists(Set<Wishlist> wishlists) {
        this.wishlists = wishlists;
    }
}