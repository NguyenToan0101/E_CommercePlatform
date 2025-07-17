package org.example.ecommerce.controller.admin;

import org.example.ecommerce.common.dto.admin.permission.AdminDTO;
import org.example.ecommerce.common.dto.admin.permission.CreateAdminRequest;
import org.example.ecommerce.service.admin.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private final AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/admins")
    public ResponseEntity<List<AdminDTO>> listAdmins() {
        return ResponseEntity.ok(adminService.getAllMods());
    }

    @PostMapping("/admins")
    public ResponseEntity<AdminDTO> createMod(@RequestBody CreateAdminRequest req) {
        AdminDTO created = adminService.createMod(req);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/admins/{id}/lock")
    public ResponseEntity<Void> lockMod(@PathVariable Integer id) {
        adminService.lockAdmin(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/admins/{id}/unlock")
    public ResponseEntity<Void> unlockMod(@PathVariable Integer id) {
        adminService.unlockAdmin(id);
        return ResponseEntity.ok().build();
    }


    //delelee
    @DeleteMapping("/admins/{id}")
    public ResponseEntity<Void> deleteMod(@PathVariable Integer id) {
        adminService.deleteMod(id);
        return ResponseEntity.noContent().build();
    }


}

