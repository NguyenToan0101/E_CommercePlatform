package org.example.ecommerce.service.customer.coin;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlayResultRequest {
    private String result; // "chan" hoặc "le"
    private String betChoice;
    private int amount;
}
