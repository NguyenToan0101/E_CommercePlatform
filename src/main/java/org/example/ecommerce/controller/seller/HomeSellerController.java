package org.example.ecommerce.controller.seller;

import jakarta.servlet.http.HttpSession;
import org.example.ecommerce.entity.Customer;
import org.example.ecommerce.entity.Seller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
@Controller
@RequestMapping("/seller")
public class HomeSellerController {



    @GetMapping
    public String goToSellerPage(HttpSession session) {
//        Customer customer = (Customer) session.getAttribute("Customer");
//
//        if (customer == null) {
//            return "redirect:/login";
//        }

//        Seller seller = customer.getSeller();

//        if (seller == null || seller.getShop() == null) {
//            return "redirect:/seller/register";
//        }

        return "seller/index";
    }

}


