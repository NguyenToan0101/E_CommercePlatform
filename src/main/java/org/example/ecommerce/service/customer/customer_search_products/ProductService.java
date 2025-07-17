package org.example.ecommerce.service.customer.customer_search_products;

import org.example.ecommerce.common.dto.admin.productManagement.AdminProductDetailDTO;
import org.example.ecommerce.common.dto.admin.productManagement.ProductDTO;

import java.util.List;

public interface ProductService {
    ProductDetail getProductDetail(Integer productId);

    //admin
    List<ProductDTO> listAllProducts();

    //danh muc chinh
    List<ProductDTO> listByMainCategory(Integer mainCategoryId);

    //xem chi tiet
    AdminProductDetailDTO getAdminProductDetail(Integer id);

    //duyet san pham
    void approveProduct(Integer productId);

    //tu choi
    void rejectProduct(Integer productId);

    // Khóa sản phẩm
    void lockProduct(Integer productId, int durationMinutes);

    // Mở khóa sản phẩm
    void unlockProduct(Integer productId);

    //xóa
    void deleteProduct(Integer productId);

}
