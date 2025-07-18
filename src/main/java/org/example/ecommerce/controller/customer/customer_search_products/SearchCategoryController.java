package org.example.ecommerce.controller.customer.customer_search_products;


import org.example.ecommerce.entity.Customer;
import org.example.ecommerce.service.customer.customer_product.ProductView;
import org.example.ecommerce.service.customer.customer_search_products.SearchProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.List;

@Controller
public class SearchCategoryController {
    @Autowired
    private SearchProductService searchProductService;

    @GetMapping("/search_category")
    public String searchByCategory(@RequestParam("categoryId") Integer categoryId, Model model, HttpSession session) {
        Customer customer = (Customer) session.getAttribute("customer");
        List<ProductView> products = searchProductService.getProductCategory(categoryId);
        model.addAttribute("customer", customer);
        model.addAttribute("products", products);
        return "/customer/customer_search_product/search_category";
    }

    @GetMapping("/search_name")
    public String searchByName(@RequestParam("keyword") String keyword, Model model, HttpSession session) {
        Customer customer = (Customer) session.getAttribute("customer");
        List<ProductView> products = searchProductService.searchByName(keyword);
        model.addAttribute("customer", customer);
        model.addAttribute("products", products);
        model.addAttribute("keyword", keyword);
        return "/customer/customer_search_product/search_name";
    }

    @GetMapping("/filter_searchName")
    public String filterByPriceAndRate(@RequestParam("keyword") String keyword,@RequestParam("price_min") BigDecimal priceMin, @RequestParam("price_max") BigDecimal priceMax, @RequestParam("rate") Integer rate, Model model, HttpSession session) {
        Customer customer = (Customer) session.getAttribute("customer");
        List<ProductView> products = searchProductService.searchByPriceAndRate(priceMin, priceMax, rate, keyword);
        model.addAttribute("customer", customer);
        model.addAttribute("products", products);
        model.addAttribute("keyword", keyword);
        model.addAttribute("priceMin", priceMin);
        model.addAttribute("priceMax", priceMax);
        model.addAttribute("rate", rate);
        return "/customer/customer_search_product/search_name";
    }
}
