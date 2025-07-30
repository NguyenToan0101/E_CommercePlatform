package org.example.ecommerce.controller.seller;

import jakarta.servlet.http.HttpSession;
import org.example.ecommerce.entity.Customer;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
@Controller
@RequestMapping("/seller")
public class HomeSellerController {


    @GetMapping
    public String goToSellerPage(HttpSession session) {
        Customer customer = (Customer) session.getAttribute("customer");

        if (customer == null) {
            return "redirect:/login";
        }

        if (customer.getSeller() == null || customer.getSeller().getShop() == null) {
            return "redirect:/sellerChannel";
        }

        return "redirect:seller/dashboard";
    }


}


