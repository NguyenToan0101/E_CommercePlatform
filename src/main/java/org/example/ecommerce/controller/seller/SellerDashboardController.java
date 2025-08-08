package org.example.ecommerce.controller.seller;

import jakarta.servlet.http.HttpSession;
import org.example.ecommerce.common.dto.seller.dashboard.SellerDashboardDTO;
import org.example.ecommerce.entity.Customer;
import org.example.ecommerce.service.seller.SellerDashboardService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/seller/dashboard")
public class SellerDashboardController {

    @Autowired
    private SellerDashboardService sellerDashboardService;


    @GetMapping
    public String showDashboard(Model model, HttpSession session) {
        Customer customer = (Customer) session.getAttribute("customer");
        if (customer == null) {
            return "redirect:/login";
        }
        
        Integer shopId = customer.getSeller().getShop().getId();
        SellerDashboardDTO dashboardData = sellerDashboardService.getDashboardDataByShopId(shopId);
        model.addAttribute("dashboardData", dashboardData);
        model.addAttribute("activePage", "dashboard");
        
        return "seller/dashboard";
    }


    @GetMapping("/api/data")
    @ResponseBody
    public ResponseEntity<SellerDashboardDTO> getDashboardData(HttpSession session) {
        Customer customer = (Customer) session.getAttribute("customer");
        if (customer == null) {
            return ResponseEntity.status(401).build();
        }
        
        Integer shopId = customer.getSeller().getShop().getId();
        SellerDashboardDTO dashboardData = sellerDashboardService.getDashboardDataByShopId(shopId);
        return ResponseEntity.ok(dashboardData);
    }

    @GetMapping("/api/data/{sellerId}")
    @ResponseBody
    public ResponseEntity<SellerDashboardDTO> getDashboardDataBySellerId(@PathVariable Integer sellerId) {
        SellerDashboardDTO dashboardData = sellerDashboardService.getDashboardData(sellerId);
        return ResponseEntity.ok(dashboardData);
    }
} 