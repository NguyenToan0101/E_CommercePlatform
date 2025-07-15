package org.example.ecommerce.common.dto.shipping;

import lombok.Data;

@Data
public class ShippingRequest {
    private String provinceFrom;
    private String districtFrom;
    private String provinceTo;
    private String districtTo;
    private double weight;
    private double length;
    private double width;
    private double height;
    private String categoryName;

    // Getters & Setters
}

