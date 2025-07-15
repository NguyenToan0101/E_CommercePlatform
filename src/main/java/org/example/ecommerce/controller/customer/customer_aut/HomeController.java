package org.example.ecommerce.controller.customer.customer_aut;

import org.example.ecommerce.entity.Customer;
import org.example.ecommerce.entity.Seller;
import org.example.ecommerce.entity.Shop;
import org.example.ecommerce.repository.UserRepository;
import org.example.ecommerce.service.customer.cusromer_aut.CustomerServiceImpl;
import org.example.ecommerce.service.customer.customer_product.CustomerProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.net.URI;
import java.util.Base64;
import java.util.Optional;

@Controller
public class HomeController {
    @Autowired
    private CustomerProductService productService;



    @GetMapping("/home")
    public String home(Model model, HttpSession session) {
        Customer customer = (Customer) session.getAttribute("customer");
        if (customer == null) {
            model.addAttribute("products", productService.getProductViews());
            model.addAttribute("categories", productService.getCategories());
            return "customer/customer_aut/home";
        }

        if (customer.getImage() != null) {
            String base64Image = Base64.getEncoder().encodeToString(customer.getImage());
            model.addAttribute("base64Image", base64Image);
        }

        model.addAttribute("customer", customer);

        model.addAttribute("products", productService.getProductViews());

        model.addAttribute("categories", productService.getCategories());

        return "customer/customer_aut/home";
    }
}
