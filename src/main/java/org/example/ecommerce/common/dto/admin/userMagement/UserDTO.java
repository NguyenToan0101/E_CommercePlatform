package org.example.ecommerce.common.dto.admin.userMagement;

import lombok.Data;

import java.math.BigDecimal;

    @Data
    public class UserDTO {
        private Integer customerid;
        private String fullName;
        private String email;
        private String phone;
        private String role;      // "Người mua" / "Người bán"
        private Boolean status;    // "Hoạt động", "Bị khóa"
        private String joinedDate;
        private Integer totalOrders;
        private BigDecimal totalSpending;

        // Thông tin chi tiết bổ sung
        private String gender;       // "M", "F", hoặc null
        private String dateOfBirth;  // yyyy-MM-dd
        private String address;


        // Nếu là người bán (Seller)
        private String idNumber;           // CCCD/CMND
        private String shopName;
        private String fullAddress;
        private String businessType;
        private String taxCode;
        private String phoneShop;
        private String invoiceEmail;
        private String manageName;
        private String statusShop;
        private Boolean express;
        private Boolean fast;
        private Boolean economy;
        private Boolean lockerDelivery;
        private Boolean bulkyItems;
    }

