package org.example.ecommerce.controller.admin;

import org.example.ecommerce.common.dto.admin.productManagement.AdminProductDetailDTO;
import org.example.ecommerce.common.dto.admin.productManagement.ProductDTO;
import org.example.ecommerce.service.customer.customer_search_products.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/products")
public class AdminProductController {
    private final ProductService productService;

    @Autowired
    public AdminProductController(ProductService productService) {
        this.productService = productService;
    }


    @GetMapping
    public List<ProductDTO> listAll(
            @RequestParam(value="mainCategoryId", required=false) Integer mainCategoryId
    ) {
        if (mainCategoryId == null) {
            return productService.listAllProducts();
        }
        return productService.listByMainCategory(mainCategoryId);
    }


    @GetMapping("/{id}")
    public ProductDTO getProductById(@PathVariable("id") Integer id) {
        return productService.listAllProducts().stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Product not found: " + id));
    }

    //xem chi tiet
    @GetMapping("/detail/{id}")
    public AdminProductDetailDTO getProductDetail(@PathVariable Integer id) {
        return productService.getAdminProductDetail(id);
    }

    //duyet san pham
    @PostMapping("/{id}/approve")
    public ResponseEntity<Void> approve(@PathVariable Integer id) {
        productService.approveProduct(id);
        return ResponseEntity.ok().build();
    }

    //tu choi
    @PostMapping("/{id}/reject")
    public ResponseEntity<Void> rejectProduct(@PathVariable("id") Integer id) {
        productService.rejectProduct(id);
        return ResponseEntity.ok().build();
    }


    //khoa san pham
    @PostMapping("/{id}/lock")
    public ResponseEntity<Void> lock(
            @PathVariable("id") Integer id,
            @RequestBody LockRequest req
    ) {
        productService.lockProduct(id, (int)req.getDurationMinutes());
        return ResponseEntity.ok().build();
    }

    //mo khoa san pham
    @PutMapping("/{id}/unlock")
    public ResponseEntity<Void> unlock(@PathVariable("id") Integer id) {
        productService.unlockProduct(id);
        return ResponseEntity.ok().build();
    }

    public static class LockRequest {
        private long durationMinutes;
        public long getDurationMinutes() { return durationMinutes; }
        public void setDurationMinutes(long durationMinutes) { this.durationMinutes = durationMinutes; }
    }

    //xoa
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable("id") Integer id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

}
