package org.example.ecommerce.common.dto.admin.permission;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class AdminDTO {
    private Integer adminid;
    private String fullname;
    private String email;
    private String phone;
    private String role;       // "Super Admin" hoặc "Mod"
    private String status;
    private LocalDateTime lastLogin;
    private LocalDateTime createdAt;
}
