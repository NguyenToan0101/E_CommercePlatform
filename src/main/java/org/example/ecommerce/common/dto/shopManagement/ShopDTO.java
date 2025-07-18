package org.example.ecommerce.common.dto.shopManagement;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ShopDTO {
    private Integer shopId;
    private String shopName;
    private String manageName;
    private String businessAddress;
    private String status;
    private String invoiceEmail;
    private String phone;
    private String businessType;
    private LocalDateTime createdAt;
    private Boolean locked;
    private LocalDateTime lockedUntil;

    public ShopDTO(Integer shopId, String shopName, String ownerName,
                   String businessAddress, String status, String invoiceEmail,
                   String phone, String businessType, LocalDateTime createdAt, Boolean locked, LocalDateTime lockedUntil) {
        this.shopId = shopId;
        this.shopName = shopName;
        this.manageName = ownerName;
        this.businessAddress = businessAddress;
        this.status = status;
        this.invoiceEmail = invoiceEmail;
        this.phone = phone;
        this.businessType = businessType;
        this.createdAt = createdAt;
        this.locked = locked;
        this.lockedUntil =lockedUntil;
    }

}

