package org.example.ecommerce.controller.seller.profile;

import org.example.ecommerce.entity.Customer;
import org.example.ecommerce.entity.Shop;
import org.example.ecommerce.service.seller.profile.ShopProfileService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice(basePackages = "org.example.ecommerce.controller.seller")
public class SellerGlobalModelAdvice {
    @Autowired
    private ShopProfileService shopProfileService;

    @ModelAttribute("shop")
    public Shop addShopToModel(HttpSession session) {
        Customer customer = (Customer) session.getAttribute("customer");
        if (customer != null && customer.getSeller() != null) {
            try {
                return shopProfileService.getShopBySeller(customer.getSeller().getId());
            } catch (Exception e) {
                // Không tìm thấy shop, trả về null
            }
        }
        return null;
    }
} 