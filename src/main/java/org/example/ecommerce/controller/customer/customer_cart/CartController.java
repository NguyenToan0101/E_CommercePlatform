package org.example.ecommerce.controller.customer.customer_cart;

import org.example.ecommerce.entity.Customer;
import org.example.ecommerce.service.customer.customer_cart.CartItemDTO;
import org.example.ecommerce.service.customer.customer_cart.CartService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/cart")
public class CartController {
    @Autowired
    private CartService cartService;

    @GetMapping
    public String viewCart(HttpSession session, Model model) {
        Customer customer = (Customer) session.getAttribute("customer");
        if (customer == null) return "redirect:/login";
        model.addAttribute("customer", customer);

        List<CartItemDTO> cartItems = cartService.getCartItemsByCustomer(customer);
        model.addAttribute("cartItems", cartItems);
        return "customer/cart/items";
    }

    // API REST cho thêm vào giỏ hàng
    @PostMapping("/api/add")
    @ResponseBody
    public ResponseEntity<?> addToCartApi(@RequestParam int productId,
                                         @RequestParam int inventoryId,
                                         @RequestParam int quantity,
                                         HttpSession session) {
        Customer customer = (Customer) session.getAttribute("customer");
        if (customer == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Bạn cần đăng nhập");
        }

        try {
            boolean added = cartService.addToCartApi(customer, productId, inventoryId, quantity);
            return ResponseEntity.ok(Map.of(
                "status", added ? "ADDED" : "UPDATED",
                "message", added ? "Đã thêm vào giỏ hàng" : "Đã cập nhật số lượng"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "error", "Có lỗi xảy ra: " + e.getMessage()
            ));
        }
    }

    // API REST cho cập nhật số lượng
    @PostMapping("/api/update")
    @ResponseBody
    public ResponseEntity<?> updateCartItemApi(@RequestParam int cartItemId,
                                              @RequestParam int quantity,
                                              HttpSession session) {
        Customer customer = (Customer) session.getAttribute("customer");
        if (customer == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Bạn cần đăng nhập");
        }

        try {
            boolean updated = cartService.updateCartItemApi(customer, cartItemId, quantity);
            if (updated) {
                return ResponseEntity.ok(Map.of(
                    "status", "UPDATED",
                    "message", "Đã cập nhật số lượng"
                ));
            } else {
                return ResponseEntity.badRequest().body(Map.of(
                    "error", "Không tìm thấy sản phẩm trong giỏ hàng"
                ));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "error", "Có lỗi xảy ra: " + e.getMessage()
            ));
        }
    }

    // API REST cho xóa khỏi giỏ hàng
    @PostMapping("/api/remove")
    @ResponseBody
    public ResponseEntity<?> removeCartItemApi(@RequestParam int cartItemId,
                                              HttpSession session) {
        Customer customer = (Customer) session.getAttribute("customer");
        if (customer == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Bạn cần đăng nhập");
        }

        try {
            boolean removed = cartService.removeCartItemApi(customer, cartItemId);
            if (removed) {
                return ResponseEntity.ok(Map.of(
                    "status", "REMOVED",
                    "message", "Đã xóa khỏi giỏ hàng"
                ));
            } else {
                return ResponseEntity.badRequest().body(Map.of(
                    "error", "Không tìm thấy sản phẩm trong giỏ hàng"
                ));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "error", "Có lỗi xảy ra: " + e.getMessage()
            ));
        }
    }

    // API REST cho lấy thông tin giỏ hàng
    @GetMapping("/api/items")
    @ResponseBody
    public ResponseEntity<?> getCartItemsApi(HttpSession session) {
        Customer customer = (Customer) session.getAttribute("customer");
        if (customer == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Bạn cần đăng nhập");
        }

        try {
            List<CartItemDTO> cartItems = cartService.getCartItemsByCustomer(customer);
            return ResponseEntity.ok(Map.of(
                "cartItems", cartItems,
                "totalItems", cartItems.size()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "error", "Có lỗi xảy ra: " + e.getMessage()
            ));
        }
    }
}