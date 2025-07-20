package org.example.ecommerce.controller.customer.customer_search_products;

import org.example.ecommerce.entity.Customer;
import org.example.ecommerce.service.customer.customer_search_products.ProductDetail;
import org.example.ecommerce.service.customer.customer_search_products.ProductService;
import org.example.ecommerce.service.customer.wishlist.WishlistService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.Base64;

@Controller
public class ProductDetailController {

    @Autowired
    private ProductService productServices;
    @Autowired
    private WishlistService wishlistService;

    @GetMapping("/detailproduct")
    public String productDetail(@RequestParam("id") Integer id, Model model, HttpSession session) {
        Customer customer = (Customer) session.getAttribute("customer");
        ProductDetail detail = productServices.getProductDetail(id);
        model.addAttribute("customer", customer);
        model.addAttribute("detail", detail);
        boolean isWishlisted = wishlistService.isWishlisted(customer, id);
        model.addAttribute("isWishlisted", isWishlisted);
        return "/customer/customer_search_product/product-detail";
    }
}
