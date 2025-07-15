package org.example.ecommerce.common.dto.analysis;

import lombok.Data;

@Data
public class PaymentMethodDTO {
    private String method;
    private int value;
    private String color;
}

