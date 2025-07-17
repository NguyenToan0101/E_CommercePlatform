package org.example.ecommerce.common.dto;


import lombok.Data;

@Data
public class ShopRegistrationDTO {
//    private String id;
    // Shop info
    private String shopName;
    private String ownerName;
    private String phone;
    private Address address;

    // Shipping settings
    private Boolean express;
    private Boolean fast;
    private Boolean economy;
    private Boolean lockerDelivery;
    private Boolean bulkyItems;

    // Tax info
    private String businessType;
    private String businessAddress;
    private String invoiceEmail;
    private String taxCode;

    // Identity verification (thuộc Seller)
    private String idNumber;
    private String frontIdImage; // base64 string
    private String backIdImage;  // base64 string

@Data
    public static class Address {
        private String province;
        private String district;
        private String ward;
        private String fullAddress;


    }
}

