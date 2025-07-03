package org.example.ecommerce.controller.admin;

import org.example.ecommerce.common.dto.shopManagement.ShopDTO;
import org.example.ecommerce.service.admin.AdminShopService;
import org.example.ecommerce.service.seller.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/shops")
public class AdminShopController {

    @Autowired
    private AdminShopService adminShopService;

    @Autowired
    private ShopService shopService;

    @GetMapping("")
    public List<ShopDTO> getAllShops() {
        return adminShopService.getAllShops();
    }

}

