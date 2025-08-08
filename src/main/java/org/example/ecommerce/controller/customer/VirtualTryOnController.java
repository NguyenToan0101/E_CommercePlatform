package org.example.ecommerce.controller.customer;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class VirtualTryOnController {

    @GetMapping("/virtual-try-on")
    public String virtualTryOn(@RequestParam("productId") Integer productId, Model model) {
        model.addAttribute("productId", productId);
        return "customer/virtual-try-on/index";
    }

    @GetMapping("/virtual-ar")
    public String virtualAR(@RequestParam("productId") Integer productId, 
                           @RequestParam("productName") String productName,
                           @RequestParam("type") String type, 
                           Model model) {
        // Validate product ID
        if (productId != 48 && productId != 49 && productId != 50) {
            return "redirect:/home";
        }
        
        model.addAttribute("productId", productId);
        model.addAttribute("productName", productName);
        model.addAttribute("type", type);
        return "redirect:/quickstart-web/virtual-ar.html?productId=" + productId + 
               "&productName=" + java.net.URLEncoder.encode(productName, java.nio.charset.StandardCharsets.UTF_8) + 
               "&type=" + type;
    }
}
