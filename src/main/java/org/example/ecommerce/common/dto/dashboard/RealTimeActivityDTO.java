package org.example.ecommerce.common.dto.dashboard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data

public class RealTimeActivityDTO {
    private String time;
    private long users;
    private long orders;
    private BigDecimal revenue;

    public RealTimeActivityDTO(String time, long users, long orders, BigDecimal revenue) {
        this.time = time;
        this.users = users;
        this.orders = orders;
        this.revenue = revenue;
    }


    public RealTimeActivityDTO() {
    }
}

