package org.example.ecommerce.common.dto.admin.promotion;

import lombok.Data;

@Data
public class OverviewDTO {
    private double revenue;
    private int order;
    private float conversionRate;
    private int newUser;



    public OverviewDTO(double revenue, int order, float conversionRate, int newUser) {
        this.revenue = revenue;
        this.order = order;
        this.conversionRate = conversionRate;
        this.newUser = newUser;
    }
}
