package org.example.ecommerce.controller.customer.wishlist;

import org.example.ecommerce.entity.Customer;
import org.example.ecommerce.service.customer.customer_product.ProductView;
import org.example.ecommerce.service.customer.wishlist.WishlistService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/wishlist")
public class WishListController {
    @Autowired
    private WishlistService wishlistService;

    @PostMapping("/toggle")
    public ResponseEntity<String> toggleWishlist(@RequestParam("productId") Integer productId,
                                                 HttpSession session) {
        Customer customer = (Customer) session.getAttribute("customer");
        if (customer == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Bạn cần đăng nhập");

        boolean added = wishlistService.toggleWishlist(customer, productId);
        return ResponseEntity.ok(added ? "ADDED" : "REMOVED");
    }

    @GetMapping
    public String viewWishlist(Model model, HttpSession session) {
        Customer customer = (Customer) session.getAttribute("customer");
        if (customer == null) {
            return "redirect:/login";
        }

        List<ProductView> wishlistProducts = wishlistService.getWishlistProductViews(customer);
        model.addAttribute("customer", customer);
        model.addAttribute("wishlistProducts", wishlistProducts);
        return "customer/wishlist/wishlist";
    }
}
