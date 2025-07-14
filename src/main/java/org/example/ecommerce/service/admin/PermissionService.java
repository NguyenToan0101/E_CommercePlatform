package org.example.ecommerce.service.admin;

import jakarta.persistence.EntityNotFoundException;
import org.example.ecommerce.entity.admin.Admin;
import org.example.ecommerce.repository.admin.AdminRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Set;

@Service
public class PermissionService {
    private final AdminRepo adminRepo;

    @Autowired
    public PermissionService(AdminRepo adminRepo) {
        this.adminRepo = adminRepo;
    }


    public Set<String> getAllowedTabs(Integer adminId) {
        if (adminId == 1) {
            return Set.of(
                    "dashboard","analytics","permissions","users","products",
                    "orders","categories","promotions","complaints","messages","settings"
            );
        }
        return adminRepo.findById(adminId)
                .orElseThrow(() -> new EntityNotFoundException("Admin not found: " + adminId))
                .getAllowedTabs();
    }


    public Set<String> updateAllowedTabs(Integer adminId, Set<String> tabs) {
        Admin admin = adminRepo.findById(adminId)
                .orElseThrow(() -> new EntityNotFoundException("Admin not found: " + adminId));
        if (adminId == 1) {
            throw new IllegalArgumentException("Không thể cập nhật quyền cho Super Admin");
        }
        admin.getAllowedTabs().clear();
        admin.getAllowedTabs().addAll(tabs);
        adminRepo.save(admin);
        return admin.getAllowedTabs();
    }


}

