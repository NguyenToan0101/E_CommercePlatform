package org.example.ecommerce.controller.admin;

import org.example.ecommerce.service.admin.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/admin/permissions")
public class PermissionController {
    private final PermissionService svc;

    @Autowired
    public PermissionController(PermissionService svc) {
        this.svc = svc;
    }

    @GetMapping("/{adminId}")
    public ResponseEntity<Set<String>> getPerms(@PathVariable Integer adminId) {
        return ResponseEntity.ok(svc.getAllowedTabs(adminId));
    }

    @PutMapping("/{adminId}")
    public ResponseEntity<Set<String>> updatePerms(
            @PathVariable Integer adminId,
            @RequestBody Set<String> tabs
    ) {
        return ResponseEntity.ok(svc.updateAllowedTabs(adminId, tabs));
    }
}
