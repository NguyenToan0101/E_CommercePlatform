package org.example.ecommerce.controller.seller.chat;

import jakarta.servlet.http.HttpSession;
import org.example.ecommerce.entity.Customer;
import org.example.ecommerce.entity.Seller;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SellerChatPageController {
    @GetMapping("/seller/chat")
    public String sellerChatPage(HttpSession session, Model model) {
        Customer customer = (Customer) session.getAttribute("customer");
        if (customer == null || customer.getSeller() == null || customer.getSeller().getId() == null)
            return "redirect:/login";
        model.addAttribute("sellerId", customer.getSeller().getId());
        return "seller/chat";
    }
}