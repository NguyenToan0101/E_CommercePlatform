package org.example.ecommerce.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.Nationalized;

import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "customer")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customerid", nullable = false)
    private Integer id;

    @OneToOne(cascade = CascadeType.ALL,mappedBy = "customer")
    private Seller seller;

    @Version
    private Integer version;

    @Size(max = 50)
    @Nationalized
    @Column(name = "firstname", length = 50)
    private String firstname;

    @Size(max = 50)
    @Nationalized
    @Column(name = "lastname", length = 50)
    private String lastname;

    @Column(name = "image")
    private byte[] image;

    @Size(max = 100)
    @NotNull
    @Nationalized
    @Column(name = "email", nullable = false, length = 100)
    private String email;

    @Size(max = 20)
    @NotNull
    @Nationalized
    @Column(name = "phone", nullable = false, length = 20)
    private String phone;

    @Column(name = "gender")
    private Character gender;

    @Column(name = "dateofbirth")
    private LocalDate dateofbirth;

    @Size(max = 255)
    @Nationalized
    @Column(name = "address")
    private String address;

    @Size(max = 255)
    @Nationalized
    @Column(name = "password")
    private String password;

    @Size(max = 20)
    @Column(name = "role", length = 20)
    private String role;

    @Column(name = "status")
    private Boolean status;

    @Column(name = "createdat")
    private Instant createdat;



    public Seller getSeller() {
        return seller;
    }

    public void setSeller(Seller seller) {
        this.seller = seller;
    }

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

    public Character getGender() {
        return gender;
    }

    public void setGender(Character gender) {
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

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Instant getCreatedat() {
        return createdat;
    }

    public void setCreatedat(Instant createdat) {
        this.createdat = createdat;
    }

}