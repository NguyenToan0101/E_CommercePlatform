package org.example.ecommerce.controller.admin;

import org.example.ecommerce.common.dto.admin.permission.AdminDTO;
import org.example.ecommerce.service.admin.AdminService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

@RestController
@RequestMapping("/api/admin")
public class MeController {

    private final AdminService adminService;

    public MeController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/me")
    public ResponseEntity<AdminDTO> getCurrentAdmin(Authentication auth) {
        AdminDTO dto = adminService.findById(2);
        return ResponseEntity.ok(dto);
    }
}
