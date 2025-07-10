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

    // Khóa shop
    @PostMapping("/{shopId} /lock")
    public ResponseEntity<?> lockShop(
            @PathVariable Integer shopId,
            @RequestBody LockRequest request
    ) {
        Optional<Shop> shopOpt = shopRepository.findById(shopId);
        if (shopOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        if (request == null || request.getDurationMinutes() == null || request.getDurationMinutes() <= 0) {
            return ResponseEntity.badRequest().body("durationMinutes is required and must be > 0");
        }

        Shop shop = shopOpt.get();
        LocalDateTime until = LocalDateTime.now().plusMinutes(request.getDurationMinutes());
        shop.setLockedUntil(until);
        shop.setLocked(true);
        shop.setStatus("LOCK"); // Đặt trạng thái là bị khóa
        shopRepository.save(shop);
        // Gửi email thông báo khóa shop
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm 'ngày' dd/MM/yyyy");
            String untilStr = until.format(formatter);
            emailService.sendShopLockedEmail(shop.getShopname(), untilStr);
        } catch (MessagingException e) {
            // Có thể log lỗi gửi email nếu cần
        }
        return ResponseEntity.ok().build();
    }

    // Mở khóa shop
    @PutMapping("/{shopId}/unlock")
    public ResponseEntity<?> unlockShop(@PathVariable Integer shopId) {
        Optional<Shop> shopOpt = shopRepository.findById(shopId);
        if (shopOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Shop shop = shopOpt.get();
        shop.setLockedUntil(null);
        shop.setLocked(false);
        shop.setStatus("ACTIVE");
        shopRepository.save(shop);
        // Gửi email thông báo mở khóa shop
        try {
            emailService.sendShopUnlockedEmail(shop.getShopname());
        } catch (MessagingException e) {
            // Có thể log lỗi gửi email nếu cần
        }
        return ResponseEntity.ok().build();
    }


}

