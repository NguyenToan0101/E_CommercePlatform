package org.example.ecommerce.controller.seller;

import jakarta.servlet.http.HttpSession;
import jakarta.websocket.Session;
import org.example.ecommerce.entity.*;

import org.example.ecommerce.service.CategoryService;
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



    @GetMapping
    public String listProducts(@RequestParam(value = "keyword", required = false) String keyword, Model model, HttpSession session) {
        Customer customer = (Customer) session.getAttribute("customer");
        if (customer == null) {
            return "redirect:/login";
        }
        Integer shopId = customer.getSeller().getShop().getId();
        List<Product> products = (keyword != null && !keyword.isEmpty())
                ? productService.searchByKeyword(keyword, shopId)
                : productService.findAllProductsByShop(shopId);

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
    public String showCreateForm(Model model, HttpSession session) {
        Customer customer = (Customer) session.getAttribute("customer");
        if (customer == null) {
            return "redirect:/login";
        }
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "seller/product/create";
    }
    @PostMapping("/save")
    public String createProduct(@ModelAttribute Product product,
                                @RequestParam("images") MultipartFile[] images,
                                HttpSession session) {
        Customer customer = (Customer) session.getAttribute("customer");
        if (customer == null) {
            return "redirect:/login";
        }
        Integer shopId = customer.getSeller().getShop().getId();
        productService.save(product, images, shopId);
        return "redirect:/seller/products";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model, HttpSession session) {
        Customer customer = (Customer) session.getAttribute("customer");
        if (customer == null) {
            return "redirect:/login";
        }
        model.addAttribute("product", productService.getById(id));
        model.addAttribute("categories", categoryService.getAllCategories());
        return "seller/product/create";
    }

    @PostMapping("/edit/{id}")
    public String updateProduct(@PathVariable Integer id,
                                @ModelAttribute Product product,
                                @RequestParam("images") MultipartFile[] images,
                                HttpSession session) {
        Customer customer = (Customer) session.getAttribute("customer");
        if (customer == null) {
            return "redirect:/login";
        }
        productService.updateProduct(id, product, images);
        return "redirect:/seller/products";
    }
    @PostMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Integer id, HttpSession session) {
        Customer customer = (Customer) session.getAttribute("customer");
        if (customer == null) {
            return "redirect:/login";
        }
        productService.softDeleteProduct(id);
        return "redirect:/seller/products";
    }

}
