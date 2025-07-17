package org.example.ecommerce.common.dto.analysis;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentMethodDTO {
    private String method;
    private int value;
    private String color;
}

