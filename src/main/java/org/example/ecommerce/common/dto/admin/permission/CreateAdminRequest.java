package org.example.ecommerce.common.dto.admin.permission;

import lombok.Data;

@Data
public class CreateAdminRequest {
    private String fullname;
    private String email;
    private String phone;
    private String password;
}
