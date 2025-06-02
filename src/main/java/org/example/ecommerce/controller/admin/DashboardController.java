package org.example.ecommerce.controller.admin;

import org.example.ecommerce.model.SaleItem;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class DashboardController {

    @GetMapping("/dashboard")
    public List<SaleItem> getSalesUpdates() {
        return List.of(
                new SaleItem(1, "6.000", 12),
                new SaleItem(2, "530", 7),
                new SaleItem(3, "10", 3),
                new SaleItem(4, "20", 5)
        );
    }
}
