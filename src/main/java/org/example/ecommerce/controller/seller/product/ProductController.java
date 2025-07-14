package org.example.ecommerce.controller.seller.product;

import jakarta.servlet.http.HttpSession;
import org.example.ecommerce.common.dto.CategoryDTO;
import org.example.ecommerce.common.dto.ProductSalesDTO;
import org.example.ecommerce.entity.*;

import org.example.ecommerce.service.CategoryService;
import org.example.ecommerce.service.seller.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.Set;
import java.util.Objects;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Controller
@RequestMapping("/seller/products")
public class ProductController {

    @Autowired private ProductService productService;
    @Autowired private CategoryService categoryService;


    @GetMapping
    public String listProducts(@RequestParam(value = "keyword", required = false) String keyword,
                               @RequestParam(value = "status", defaultValue = "all") String status,
                               @RequestParam(value = "category", required = false) Integer categoryId,
                               @RequestParam(value = "page", defaultValue = "1") int page,
                               @RequestParam(value = "size", defaultValue = "12") int size,
                               Model model, HttpSession session) {
        Customer customer = (Customer) session.getAttribute("customer");
        if (customer == null) return "redirect:/login";

        Integer shopId = customer.getSeller().getShop().getId();
        String backendStatus = mapStatus(status);
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<ProductSalesDTO> productPage = productService.getProductSalesData(shopId, backendStatus, keyword, categoryId, pageable);

        Map<String, Long> statusCounts = productService.getStatusCounts(shopId);
        Long totalProducts = productService.getTotalProductCount(shopId);
        List<CategoryDTO> allCategories = categoryService.convertToDTOList(categoryService.getRootCategories());

        model.addAttribute("products", productPage.getContent());
        model.addAttribute("keyword", keyword);
        model.addAttribute("countAll", totalProducts);
        model.addAttribute("countAvailable", statusCounts.get("available"));
        model.addAttribute("countViolation", statusCounts.get("violation"));
        model.addAttribute("countPending", statusCounts.get("pending_approval"));
        model.addAttribute("countInactive", statusCounts.get("INACTIVE"));
        model.addAttribute("selectedStatus", status);
        model.addAttribute("categories", allCategories);
        model.addAttribute("selectedCategory", categoryId);
        model.addAttribute("currentPage", page);
        model.addAttribute("perPage", size);
        model.addAttribute("totalPages", productPage.getTotalPages());
        model.addAttribute("totalElements", productPage.getTotalElements());
        return "seller/product/products";
    }

    private String mapStatus(String frontendStatus) {
        return switch (frontendStatus) {
            case "active" -> "available";
            case "draft" -> "INACTIVE";
            case "violation" -> "violation";
            case "pending" -> "pending_approval";
            default -> null;
        };
    }
    
    private List<ProductSalesDTO> filterProducts(List<ProductSalesDTO> products, String keyword, Integer categoryId) {
        if (keyword != null && !keyword.isEmpty()) {
            String lowerCaseKeyword = keyword.toLowerCase();
            products = products.stream()
                    .filter(p -> p.getProductName().toLowerCase().contains(lowerCaseKeyword))
                    .collect(Collectors.toList());
        }
        if (categoryId != null) {
            products = products.stream()
                    .filter(p -> p.getCategoryId() != null && categoryService.isCategoryOrSubcategory(categoryId, p.getCategoryId()))
                    .collect(Collectors.toList());
        }
        return products;
    }

    @GetMapping("/create")
    public String showCreateForm(Model model, HttpSession session) {
        Customer customer = (Customer) session.getAttribute("customer");
        if (customer == null) return "redirect:/login";

        Product product = new Product();
        product.setUseVariantShipping(false);
        List<Inventory> inventories = new ArrayList<>();
        inventories.add(new Inventory());
        product.setInventories(inventories);

        model.addAttribute("product", product);
        model.addAttribute("shopId", customer.getSeller().getShop().getId());

        List<CategoryDTO> rootCategoryDTOs = categoryService.convertToDTOList(categoryService.getRootCategories());
        model.addAttribute("rootCategories", rootCategoryDTOs);

        return "seller/product/create";
    }

    @PostMapping("/save")
    public String createProduct(@Valid @ModelAttribute("product") Product product,
                                BindingResult bindingResult,
                                @RequestParam("images") MultipartFile[] images,
                                @RequestParam(value = "inventoryImages", required = false) MultipartFile[] inventoryImages,
                                HttpSession session, Model model) {
        Customer customer = (Customer) session.getAttribute("customer");
        if (customer == null) return "redirect:/login";

        if (bindingResult.hasErrors()) {
            System.out.println("[DEBUG] Lỗi validate khi tạo sản phẩm:");
            bindingResult.getAllErrors().forEach(error -> System.out.println(error.toString()));
            List<CategoryDTO> rootCategoryDTOs = categoryService.convertToDTOList(categoryService.getRootCategories());
            model.addAttribute("rootCategories", rootCategoryDTOs);
            return "seller/product/create";
        }

        Integer shopId = customer.getSeller().getShop().getId();
        productService.save(product, images, inventoryImages, shopId);
        return "redirect:/seller/products";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model, HttpSession session) {
        Customer customer = (Customer) session.getAttribute("customer");
        if (customer == null) return "redirect:/login";

        Product product = productService.getById(id);
        model.addAttribute("product", product);
        List<CategoryDTO> rootCategoryDTOs = categoryService.convertToDTOList(categoryService.getRootCategories());
        model.addAttribute("rootCategories", rootCategoryDTOs);
        // Thêm xử lý unique size/color
        if (product.getInventories() != null && product.getInventories().size() > 1) {
            Set<String> sizeSet = product.getInventories().stream()
                .map(inv -> inv.getDimension())
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
            Set<String> colorSet = product.getInventories().stream()
                .map(inv -> inv.getColor())
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
            model.addAttribute("sizeSet", sizeSet);
            model.addAttribute("colorSet", colorSet);
        }
        return "seller/product/create";
    }

    @PostMapping("/edit/{id}")
    public String updateProduct(@PathVariable Integer id,
                                @ModelAttribute Product product,
                                @RequestParam("images") MultipartFile[] images,
                                @RequestParam(value = "inventoryImages", required = false) MultipartFile[] inventoryImages,
                                HttpSession session) {
        Customer customer = (Customer) session.getAttribute("customer");
        if (customer == null) return "redirect:/login";

        productService.updateProduct(id, product, images, inventoryImages);
        return "redirect:/seller/products";
    }

    @PostMapping("/hide")
    public String hideProducts(@RequestParam("ids") List<Integer> ids, HttpSession session) {
        Customer customer = (Customer) session.getAttribute("customer");
        if (customer == null) return "redirect:/login";
        productService.softDeleteProducts(ids);
        return "redirect:/seller/products";
    }

    @PostMapping("/show")
    public String showProducts(@RequestParam("ids") List<Integer> ids, HttpSession session) {
        Customer customer = (Customer) session.getAttribute("customer");
        if (customer == null) return "redirect:/login";
        productService.showProducts(ids);
        return "redirect:/seller/products";
    }

    @PostMapping("/stock-alert")
    public String setStockAlert(@RequestParam("inventoryId") List<Integer> inventoryIds,
                                @RequestParam("alertQuantity") List<Integer> alertQuantities,
                                HttpSession session) {
        Map<Integer, Integer> alertMap = new HashMap<>();
        for (int i = 0; i < inventoryIds.size(); i++) {
            alertMap.put(inventoryIds.get(i), alertQuantities.get(i));
        }
        session.setAttribute("stockAlertMap", alertMap);
        return "redirect:/seller/products";
    }
}
