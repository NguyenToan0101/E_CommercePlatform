package org.example.ecommerce.controller.customer.chat;

import jakarta.servlet.http.HttpSession;
import org.example.ecommerce.entity.Customer;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CustomerChatPageController {
    @GetMapping("/customer/chat")
    public String customerChatPage(HttpSession session, Model model) {
        Customer customer = (Customer) session.getAttribute("customer");
        if (customer == null) return "redirect:/login";
        model.addAttribute("customerId", customer.getId());
        return "customer/chat";
    }
}