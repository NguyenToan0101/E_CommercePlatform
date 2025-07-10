package org.example.ecommerce.common.dto.shopManagement;

import java.time.LocalDateTime;

public class ShopDetailDTO {
    private Integer shopId;
    private String shopName;
    private String manageName;
    private String businessAddress;
    private String createdAt;
    private String status;
    private String invoiceEmail;
    private String phone;
    private String businessType;
    private String description;
    private String imageShop;
    private String taxCode;
    private String mainCategory;
    private Boolean locked;
    private LocalDateTime lockedUntil;

    // Từ bảng Customer
    private String customerEmail;
    private String gender;
    private String dob;
    private String address;

    // Từ bảng Seller
    private String idNumber;
    private String frontIdImage;
    private String backIdImage;

    // ✅ Constructor chính xác 100% với JPQL
    public ShopDetailDTO(
            Integer shopId,
            String shopName,
            String manageName,
            String businessAddress,
            String createdAt,       // Đã đổi từ LocalDateTime sang String
            String status,
            String invoiceEmail,
            String phone,
            String businessType,
            String description,
            String imageShop,
            String taxCode,
            String mainCategory,
            String customerEmail,
            String gender,
            String dob,             // Đã đổi từ LocalDate sang String
            String address,
            String idNumber,
            String frontIdImage,
            String backIdImage
    ) {
        this.shopId = shopId;
        this.shopName = shopName;
        this.manageName = manageName;
        this.businessAddress = businessAddress;
        this.createdAt = createdAt;
        this.status = status;
        this.invoiceEmail = invoiceEmail;
        this.phone = phone;
        this.businessType = businessType;
        this.description = description;
        this.imageShop = imageShop;
        this.taxCode = taxCode;
        this.mainCategory = mainCategory;
        this.customerEmail = customerEmail;
        this.gender = gender;
        this.dob = dob;
        this.address = address;
        this.idNumber = idNumber;
        this.frontIdImage = frontIdImage;
        this.backIdImage = backIdImage;
    }

    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getManageName() {
        return manageName;
    }

    public void setManageName(String manageName) {
        this.manageName = manageName;
    }

    public String getBusinessAddress() {
        return businessAddress;
    }

    public void setBusinessAddress(String businessAddress) {
        this.businessAddress = businessAddress;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getInvoiceEmail() {
        return invoiceEmail;
    }

    public void setInvoiceEmail(String invoiceEmail) {
        this.invoiceEmail = invoiceEmail;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageShop() {
        return imageShop;
    }

    public void setImageShop(String imageShop) {
        this.imageShop = imageShop;
    }

    public String getTaxCode() {
        return taxCode;
    }

    public void setTaxCode(String taxCode) {
        this.taxCode = taxCode;
    }

    public String getMainCategory() {
        return mainCategory;
    }

    public void setMainCategory(String mainCategory) {
        this.mainCategory = mainCategory;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
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

    public String getFrontIdImage() {
        return frontIdImage;
    }

    public void setFrontIdImage(String frontIdImage) {
        this.frontIdImage = frontIdImage;
    }

    public String getBackIdImage() {
        return backIdImage;
    }

    public void setBackIdImage(String backIdImage) {
        this.backIdImage = backIdImage;
    }
}
