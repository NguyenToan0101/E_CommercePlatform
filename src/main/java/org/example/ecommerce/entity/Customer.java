package org.example.ecommerce.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;
import java.time.LocalDate;
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

    @Column(name = "status", length = Integer.MAX_VALUE)
    private String status;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "createdat")
    private Instant createdat;

    @Column(name = "image", length = Integer.MAX_VALUE)
    private String image;

    @Column(name = "version")
    private Long version;

    @OneToMany(mappedBy = "adminid")
    private Set<Adminlog> adminlogs = new LinkedHashSet<>();

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

    @OneToOne
    private Seller seller;

    @OneToMany(mappedBy = "customerid")
    private Set<Userpromotion> userpromotions = new LinkedHashSet<>();

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Instant getCreatedat() {
        return createdat;
    }

    public void setCreatedat(Instant createdat) {
        this.createdat = createdat;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public Set<Adminlog> getAdminlogs() {
        return adminlogs;
    }

    public void setAdminlogs(Set<Adminlog> adminlogs) {
        this.adminlogs = adminlogs;
    }

    public Set<Cart> getCarts() {
        return carts;
    }

    public void setCarts(Set<Cart> carts) {
        this.carts = carts;
    }

    public Set<Complaint> getComplaints() {
        return complaints;
    }

    public void setComplaints(Set<Complaint> complaints) {
        this.complaints = complaints;
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

    public Set<Review> getReviews() {
        return reviews;
    }

    public void setReviews(Set<Review> reviews) {
        this.reviews = reviews;
    }

    public Seller getSeller() {
        return seller;
    }

    public void setSeller(Seller seller) {
        this.seller = seller;
    }

    public Set<Userpromotion> getUserpromotions() {
        return userpromotions;
    }

    public void setUserpromotions(Set<Userpromotion> userpromotions) {
        this.userpromotions = userpromotions;
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