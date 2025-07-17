package org.example.ecommerce.common.dto;

import lombok.Data;

import java.util.List;

@Data
public class AdminLoginResponse {
    private String message;
    private String token;
    private String redirectUrl;
    private List<String> allowedTabs;


}
