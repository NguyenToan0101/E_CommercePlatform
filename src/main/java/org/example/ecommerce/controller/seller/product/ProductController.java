package org.example.ecommerce.controller.seller.product;

import jakarta.servlet.http.HttpSession;
import org.example.ecommerce.common.dto.CategoryDTO;
import org.example.ecommerce.common.dto.seller.ProductSalesDTO;
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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.example.ecommerce.common.dto.seller.InventorySimpleDTO;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/seller/products")
public class ProductController {

    @Autowired private ProductService productService;
    @Autowired private CategoryService categoryService;


    @GetMapping
    public String listProducts(@RequestParam(value = "keyword", required = false) String keyword,
                               @RequestParam(value = "status", defaultValue = "all" ) String status,
                               @RequestParam(value = "category", required = false) Integer categoryId,
                               @RequestParam(value = "page", defaultValue = "1") int page,
                               @RequestParam(value = "size", defaultValue = "12") int size,
                               Model model, HttpSession session) {

        Customer customer = (Customer) session.getAttribute("customer");
        if (customer == null)
            return "redirect:/login";

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
        model.addAttribute("countViolation", statusCounts.get("locked"));
        model.addAttribute("countPending", statusCounts.get("pending_approval"));
        model.addAttribute("countInactive", statusCounts.get("hidding"));
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
            case "draft" -> "hidding";
            case "violation" -> "locked";
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
                                @RequestParam(value = "colorNames", required = false) String[] colorNames,
                                @RequestParam(value = "colorImages", required = false) MultipartFile[] colorImages,
                                HttpSession session, Model model,
                                RedirectAttributes redirectAttributes) {
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
        productService.save(product, images, colorNames, colorImages, shopId);
        redirectAttributes.addFlashAttribute("successMessage", "Tạo sản phẩm thành công!");
        return "redirect:/seller/products";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model, HttpSession session) throws JsonProcessingException {
        Customer customer = (Customer) session.getAttribute("customer");
        if (customer == null) return "redirect:/login";

        Product product = productService.getById(id);
        model.addAttribute("product", product);
        List<CategoryDTO> rootCategoryDTOs = categoryService.convertToDTOList(categoryService.getRootCategories());
        model.addAttribute("rootCategories", rootCategoryDTOs);

        // Map inventories sang DTO đơn giản
        List<InventorySimpleDTO> inventoryDTOs = product.getInventories().stream().map(inv -> {
            InventorySimpleDTO dto = new InventorySimpleDTO();
            dto.color = inv.getColor();
            dto.dimension = inv.getDimension();
            dto.quantity = inv.getQuantity();
            dto.price = inv.getPrice();
            dto.weight = inv.getWeight();
            dto.length = inv.getLength();
            dto.width = inv.getWidth();
            dto.height = inv.getHeight();
            dto.image = inv.getImage();
            return dto;
        }).collect(Collectors.toList());

        model.addAttribute("inventoriesJson", new ObjectMapper().writeValueAsString(inventoryDTOs));
        return "seller/product/create";
    }

    @PostMapping("/edit/{id}")
    public String updateProduct(@PathVariable Integer id,
                                @ModelAttribute Product product,
                                @RequestParam("images") MultipartFile[] images,
                                @RequestParam(value = "colorNames", required = false) String[] colorNames,
                                @RequestParam(value = "colorImages", required = false) MultipartFile[] colorImages,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {
        Customer customer = (Customer) session.getAttribute("customer");
        if (customer == null) return "redirect:/login";
        try {
            productService.updateProduct(id, product, images, colorNames, colorImages);
            redirectAttributes.addFlashAttribute("successMessage", "Cập nhật sản phẩm thành công!");
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/seller/products";
    }

    @PostMapping("/hide")
    public String hideProducts(@RequestParam("ids") List<Integer> ids, HttpSession session, RedirectAttributes redirectAttributes) {
        Customer customer = (Customer) session.getAttribute("customer");
        if (customer == null) return "redirect:/login";
        try {
            productService.softDeleteProducts(ids);
            redirectAttributes.addFlashAttribute("successMessage", "Ẩn sản phẩm thành công!");
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/seller/products";
        }
        return "redirect:/seller/products";
    }

    @PostMapping("/show")
    public String showProducts(@RequestParam("ids") List<Integer> ids, HttpSession session, RedirectAttributes redirectAttributes) {
        Customer customer = (Customer) session.getAttribute("customer");
        if (customer == null) return "redirect:/login";
        try {
            productService.showProducts(ids);
            redirectAttributes.addFlashAttribute("successMessage", "Hiển thị sản phẩm thành công!");
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/seller/products";
        }
        return "redirect:/seller/products";
    }

    @PostMapping("/delete")
    public String deleteProduct(@RequestParam("id") Integer id, RedirectAttributes redirectAttributes, HttpSession session) {
        Customer customer = (Customer) session.getAttribute("customer");
        if (customer == null) return "redirect:/login";
        try {
            productService.deleteProductHard(id);
            redirectAttributes.addFlashAttribute("successMessage", "Xóa sản phẩm thành công!");
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/seller/products";
    }


    @PostMapping("/inventory/alert")
    public String updateInventoryAlerts(
        @RequestParam("productId") Integer productId,
        @RequestParam("inventoryId") List<Integer> inventoryIds,
        @RequestParam(value = "alertQuantity", required = false) List<Integer> alertQuantities,
        @RequestParam(value = "setAllAlertQuantity", required = false) Integer setAllAlertQuantity,
        RedirectAttributes redirectAttributes) {
    try {
        if (setAllAlertQuantity != null) {
            productService.updateAllInventoriesAlertQuantity(productId, setAllAlertQuantity);
        } else {
            productService.updateInventoriesAlertQuantity(inventoryIds, alertQuantities);
        }
        redirectAttributes.addFlashAttribute("successMessage", "Cập nhật cảnh báo tồn kho thành công!");
    } catch (Exception e) {
        redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
    }
    return "redirect:/seller/products";
}
}
