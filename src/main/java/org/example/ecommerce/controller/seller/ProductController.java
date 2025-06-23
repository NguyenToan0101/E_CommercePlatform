package org.example.ecommerce.controller.seller;

import org.example.ecommerce.entity.Category;
import org.example.ecommerce.entity.Inventory;
import org.example.ecommerce.entity.Product;
import org.example.ecommerce.service.StorageService;
import org.example.ecommerce.service.seller.CategoryService;
import org.example.ecommerce.service.seller.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequestMapping("/seller/products")
public class ProductController {

    @Autowired private ProductService productService;
    @Autowired private CategoryService categoryService;
    @Autowired private StorageService storageService;


    @GetMapping
    public String listProducts(@RequestParam(value = "keyword", required = false) String keyword, Model model) {
        List<Product> products = (keyword != null && !keyword.isEmpty())
                ? productService.searchByKeyword(keyword)
                : productService.findAllProducts();

        model.addAttribute("products", products);
        model.addAttribute("keyword", keyword);

        int totalQuantity = products.stream()
                .flatMap(product -> product.getInventories().stream())
                .mapToInt(Inventory::getQuantity)
                .sum();

        model.addAttribute("totalQuantity", totalQuantity);

        return "seller/product/products";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "seller/product/create";
    }

    @PostMapping("/save")
    public String createProduct(@ModelAttribute Product product,
                                @RequestParam("images") MultipartFile[] images) {

        productService.save(product, images);
        return "redirect:/seller/products";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {
        model.addAttribute("product", productService.getById(id));
        model.addAttribute("categories", categoryService.getAllCategories());
        return "seller/product/create";
    }


}
