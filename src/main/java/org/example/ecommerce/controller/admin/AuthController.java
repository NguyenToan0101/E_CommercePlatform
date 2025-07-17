package org.example.ecommerce.controller.admin;

import org.example.ecommerce.common.dto.AdminLoginRequest;

import org.example.ecommerce.service.admin.AdminService;

import org.springframework.beans.factory.annotation.Value;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@Controller
@RequestMapping("/admin")
public class AuthController {
    private final AdminService adminService;
    @Value("${api.frontend.admin}")
    private String frontendAdminUrl;


    public AuthController( AdminService adminService) {
        this.adminService = adminService;
    }


    @PostMapping("/login")
    public ResponseEntity<?> registrationSeller(@RequestBody AdminLoginRequest adminLoginRequest ) {
        try {
            if (adminService.isAdmin(adminLoginRequest.getEmail(), adminLoginRequest.getPassword())) {
                Map<String, Object> responseBody = new HashMap<>();
                responseBody.put("email", adminLoginRequest.getEmail());
                return ResponseEntity.ok(responseBody);
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Email hoặc mật khẩu không đúng");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi server. Vui lòng thử lại.");
        }


    }
}
