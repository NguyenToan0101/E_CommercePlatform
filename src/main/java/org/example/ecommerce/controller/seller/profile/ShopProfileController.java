package org.example.ecommerce.controller.seller.profile;

import jakarta.servlet.http.HttpSession;
import org.example.ecommerce.entity.Customer;
import org.example.ecommerce.entity.Shop;
import org.example.ecommerce.entity.Seller;
import org.example.ecommerce.service.seller.profile.ShopProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

@Controller
public class ShopProfileController {
    @Autowired
    private ShopProfileService shopProfileService;

    @GetMapping("/seller/shop/profile")
    public String viewProfile(HttpSession session, Model model) {
        Customer customer = (Customer) session.getAttribute("customer");
        if (customer == null) return "redirect:/login";
        Shop shop = shopProfileService.getShopBySeller(customer.getSeller().getId());
        Seller seller = customer.getSeller();
        model.addAttribute("shop", shop);
        model.addAttribute("seller", seller);
        return "seller/shop/profile";
    }

    @GetMapping("/seller/shop/profile/edit")
    public String editProfile(HttpSession session, Model model) {
        Customer customer = (Customer) session.getAttribute("customer");
        if (customer == null) return "redirect:/login";
        Shop shop = shopProfileService.getShopBySeller(customer.getSeller().getId());
        Seller seller = customer.getSeller();
        model.addAttribute("shop", shop);
        model.addAttribute("seller", seller);
        return "seller/shop/profile_edit";
    }

    @PostMapping("/seller/shop/profile")
    public String updateProfile(@ModelAttribute Shop shop,
                                @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
                                HttpSession session, RedirectAttributes redirectAttributes) {
        Customer customer = (Customer) session.getAttribute("customer");
        if (customer == null) return "redirect:/login";
        try {
            shopProfileService.updateShopProfile(customer.getSeller().getId(), shop, imageFile);
            redirectAttributes.addFlashAttribute("successMessage", "Cập nhật hồ sơ shop thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/seller/shop/profile";
    }
} 