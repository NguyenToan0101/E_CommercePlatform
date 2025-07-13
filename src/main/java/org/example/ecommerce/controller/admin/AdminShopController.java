package org.example.ecommerce.controller.admin;

import jakarta.mail.MessagingException;
import org.example.ecommerce.common.dto.admin.userManagement.LockRequest;
import org.example.ecommerce.common.dto.shopManagement.ShopDTO;
import org.example.ecommerce.common.dto.shopManagement.ShopDetailDTO;
import org.example.ecommerce.entity.Shop;
import org.example.ecommerce.repository.ShopRepository;
import org.example.ecommerce.service.admin.AdminShopService;
import org.example.ecommerce.service.customer.cusromer_aut.EmailService;
import org.example.ecommerce.service.seller.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/admin/shops")
public class AdminShopController {

    @Autowired
    private AdminShopService adminShopService;


    @Autowired
    private ShopRepository shopRepository;

    @Autowired
    private EmailService emailService;

    @GetMapping("")
    public List<ShopDTO> getAllShops() {
        return adminShopService.getAllShops();
    }

    @GetMapping("/{shopId}")
    public ResponseEntity<ShopDetailDTO> getShopById(@PathVariable Integer shopId) {
        return ResponseEntity.ok(adminShopService.getShopDetail(shopId));
    }

    //Duyet Shop
    @PostMapping("/{shopId}/approve")
    public ResponseEntity<Void> approveShop(@PathVariable Integer shopId) {
        adminShopService.approveShop(shopId);
        return ResponseEntity.noContent().build();
    }

    // Từ chối shop
    @PostMapping("/{shopId}/reject")
    public ResponseEntity<Void> rejectShop(@PathVariable Integer shopId) {
        adminShopService.rejectShop(shopId);
        return ResponseEntity.noContent().build();
    }


    // DTO để nhận content { "durationMinutes": 123 }
    public static class LockRequest {
        private Integer durationMinutes;
        public Integer getDurationMinutes() { return durationMinutes; }
        public void setDurationMinutes(Integer durationMinutes) { this.durationMinutes = durationMinutes; }
    }

    // Khóa shop
    @PostMapping("/{shopId}/lock")
    public ResponseEntity<Void> lockShop(
            @PathVariable Integer shopId,
            @RequestBody LockRequest req
    ) {
        adminShopService.lockShop(shopId, req.getDurationMinutes());
        return ResponseEntity.noContent().build();
    }

    // Mở khóa shop
    @PutMapping("/{shopId}/unlock")
    public ResponseEntity<Void> unlockShop(@PathVariable Integer shopId) {
        adminShopService.unlockShop(shopId);
        return ResponseEntity.noContent().build();
    }


}

