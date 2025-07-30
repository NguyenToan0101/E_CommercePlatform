package org.example.ecommerce.controller.seller.product;

import jakarta.servlet.http.HttpSession;
import org.example.ecommerce.entity.Customer;
import org.example.ecommerce.entity.Product;
import org.example.ecommerce.service.seller.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/seller/products")
class ProductSellerApiController {
    @Autowired
    private ProductService productService;

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getSellerProducts(HttpSession session) {
        Customer customer = (Customer) session.getAttribute("customer");
        if (customer == null || customer.getSeller() == null) return ResponseEntity.ok(List.of());
        Integer shopId = customer.getSeller().getShop().getId();
        // Lấy tất cả sản phẩm của shop, chỉ trả về id và tên
        List<Product> products = productService.getAllProductsForShop(shopId);
        List<Map<String, Object>> result = products.stream().map(p -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", p.getId());
            map.put("name", p.getName());
            return map;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }
}
