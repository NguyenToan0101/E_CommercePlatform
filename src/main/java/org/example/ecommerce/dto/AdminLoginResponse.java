package org.example.ecommerce.dto;

import lombok.Data;

@Data
public class AdminLoginResponse {
    private String message;
    private String token;
    private String redirectUrl;


}
