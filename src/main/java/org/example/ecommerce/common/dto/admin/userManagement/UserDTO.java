package org.example.ecommerce.common.dto.admin.userManagement;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

public class UserDTO {
    private Integer customerid;
    private String fullName;
    private String email;
    private String phone;
    private String role;
    private Boolean status;
    private Instant joinedDate;
    private Long totalOrders;
    private BigDecimal totalSpending;
    private String gender;
    private LocalDate dateOfBirth;
    private String address;
    private String idNumber;
    private String shopName;
    private String fullAddress;
    private String businessType;
    private String taxCode;
    private String shopPhone;
    private String invoiceEmail;
    private String manageName;
    private String statusShop;
    private Boolean express;
    private Boolean fast;
    private Boolean economy;
    private Boolean lockerDelivery;
    private Boolean bulkyItems;

    // ✅ Constructor khớp hoàn toàn với JPQL projection
    public UserDTO(Integer customerid, String fullName, String email, String phone, String role, Boolean status,
                   Instant joinedDate, Long totalOrders, BigDecimal totalSpending,
                   String gender, LocalDate dateOfBirth, String address,
                   String idNumber, String shopName, String fullAddress,
                   String businessType, String taxCode, String shopPhone,
                   String invoiceEmail, String manageName, String statusShop,
                   Boolean express, Boolean fast, Boolean economy,
                   Boolean lockerDelivery, Boolean bulkyItems) {
        this.customerid = customerid;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.role = role;
        this.status = status;
        this.joinedDate = joinedDate;
        this.totalOrders = totalOrders;
        this.totalSpending = totalSpending;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
        this.idNumber = idNumber;
        this.shopName = shopName;
        this.fullAddress = fullAddress;
        this.businessType = businessType;
        this.taxCode = taxCode;
        this.shopPhone = shopPhone;
        this.invoiceEmail = invoiceEmail;
        this.manageName = manageName;
        this.statusShop = statusShop;
        this.express = express;
        this.fast = fast;
        this.economy = economy;
        this.lockerDelivery = lockerDelivery;
        this.bulkyItems = bulkyItems;
    }

    public Integer getCustomerid() {
        return customerid;
    }

    public void setCustomerid(Integer customerid) {
        this.customerid = customerid;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
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

    public Instant getJoinedDate() {
        return joinedDate;
    }

    public void setJoinedDate(Instant joinedDate) {
        this.joinedDate = joinedDate;
    }

    public Long getTotalOrders() {
        return totalOrders;
    }

    public void setTotalOrders(Long totalOrders) {
        this.totalOrders = totalOrders;
    }

    public BigDecimal getTotalSpending() {
        return totalSpending;
    }

    public void setTotalSpending(BigDecimal totalSpending) {
        this.totalSpending = totalSpending;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getFullAddress() {
        return fullAddress;
    }

    public void setFullAddress(String fullAddress) {
        this.fullAddress = fullAddress;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getTaxCode() {
        return taxCode;
    }

    public void setTaxCode(String taxCode) {
        this.taxCode = taxCode;
    }

    public String getShopPhone() {
        return shopPhone;
    }

    public void setShopPhone(String shopPhone) {
        this.shopPhone = shopPhone;
    }

    public String getInvoiceEmail() {
        return invoiceEmail;
    }

    public void setInvoiceEmail(String invoiceEmail) {
        this.invoiceEmail = invoiceEmail;
    }

    public String getManageName() {
        return manageName;
    }

    public void setManageName(String manageName) {
        this.manageName = manageName;
    }

    public String getStatusShop() {
        return statusShop;
    }

    public void setStatusShop(String statusShop) {
        this.statusShop = statusShop;
    }

    public Boolean getExpress() {
        return express;
    }

    public void setExpress(Boolean express) {
        this.express = express;
    }

    public Boolean getFast() {
        return fast;
    }

    public void setFast(Boolean fast) {
        this.fast = fast;
    }

    public Boolean getEconomy() {
        return economy;
    }

    public void setEconomy(Boolean economy) {
        this.economy = economy;
    }

    public Boolean getLockerDelivery() {
        return lockerDelivery;
    }

    public void setLockerDelivery(Boolean lockerDelivery) {
        this.lockerDelivery = lockerDelivery;
    }

    public Boolean getBulkyItems() {
        return bulkyItems;
    }

    public void setBulkyItems(Boolean bulkyItems) {
        this.bulkyItems = bulkyItems;
    }
}
