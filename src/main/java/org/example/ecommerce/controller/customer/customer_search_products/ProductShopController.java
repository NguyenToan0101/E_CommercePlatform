package org.example.ecommerce.controller.customer.customer_search_products;

import jakarta.servlet.http.HttpSession;
import org.example.ecommerce.entity.Customer;
import org.example.ecommerce.entity.Shop;
import org.example.ecommerce.service.customer.customer_product.CustomerProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class ProductShopController {
    @Autowired
    private CustomerProductService productService;

    @GetMapping("/shop_detail")
    public String showProductsShop(@RequestParam("shopid") Integer shopid, Model model, HttpSession session) {
        Customer customer = (Customer) session.getAttribute("customer");
        model.addAttribute("customer", customer);
        model.addAttribute("shop", productService.getShops(shopid));
        model.addAttribute("products", productService.getProductViewsByShopId(shopid));
        return "customer/customer_search_product/search_shop";
    }
}
