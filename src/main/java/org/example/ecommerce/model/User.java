package org.example.ecommerce.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "customer")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int customerid;
    @Column(name = "firstname")
    private String firtstname;
    @Column(name = "lastname")
    private String lastName;
    private String email;
    private String phone;
    private String gender;
    private Date dateofbirth;
    private String address;
    private String role;
    private boolean status;
    @Column(name = "createdat")
    private LocalDateTime createdAt;
    private String image;
    private String password;

    public User(int customerid, String firtstname, String lastName, String phone, String email, String gender, Date dateofbirth, String address, String role, boolean status, LocalDateTime createdAt, String image, String password) {
        this.customerid = customerid;
        this.firtstname = firtstname;
        this.lastName = lastName;
        this.phone = phone;
        this.email = email;
        this.gender = gender;
        this.dateofbirth = dateofbirth;
        this.address = address;
        this.role = role;
        this.status = status;
        this.createdAt = createdAt;
        this.image = image;
        this.password = password;
    }

    public User() {

    }

    public int getCustomerid() {
        return customerid;
    }

    public void setCustomerid(int customerid) {
        this.customerid = customerid;
    }

    public String getFirtstname() {
        return firtstname;
    }

    public void setFirtstname(String firtstName) {
        this.firtstname = firtstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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

    public Date getDateofbirth() {
        return dateofbirth;
    }

    public void setDateofbirth(Date dateOfBirth) {
        this.dateofbirth = dateOfBirth;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime creatAt) {
        this.createdAt = creatAt;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String images) {
        this.image = images;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "password='" + password + '\'' +
                ", images='" + image + '\'' +
                ", creatAt=" + createdAt +
                ", status=" + status +
                ", role='" + role + '\'' +
                ", address='" + address + '\'' +
                ", dateOfBirth=" + dateofbirth +
                ", gender='" + gender + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", lastName='" + lastName + '\'' +
                ", firtstName='" + firtstname + '\'' +
                ", id=" + customerid +
                '}';
    }
}
