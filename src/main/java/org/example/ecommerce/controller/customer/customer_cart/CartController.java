package org.example.ecommerce.controller.customer.customer_cart;

import org.example.ecommerce.entity.Customer;
import org.example.ecommerce.service.customer.customer_cart.CartItemDTO;
import org.example.ecommerce.service.customer.customer_cart.CartService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController {
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public String viewCart(HttpSession session, Model model) {
        Customer customer = (Customer) session.getAttribute("customer");
        if (customer == null) return "redirect:/login";
        model.addAttribute("customer", customer);

        List<CartItemDTO> cartItems = cartService.getCartItemsByCustomer(customer);
        model.addAttribute("cartItems", cartItems);
        return "customer/cart/items";
    }

    @PostMapping("/add")
    public String addToCart(HttpSession session,
                            @RequestParam int productId,
                            @RequestParam int inventoryId,
                            @RequestParam int quantity) {
        Customer customer = (Customer) session.getAttribute("customer");
        if (customer == null) return "redirect:/login";

        cartService.addToCart(customer, productId, inventoryId, quantity);
        return "redirect:/cart";
    }

    @PostMapping("/update")
    public String updateCartItem(@RequestParam int cartItemId,
                                 @RequestParam int quantity) {
        cartService.updateCartItem(cartItemId, quantity);
        return "redirect:/cart";
    }

    @PostMapping("/remove")
    public String removeCartItem(@RequestParam int cartItemId) {
        cartService.removeCartItem(cartItemId);
        return "redirect:/cart";
    }
}