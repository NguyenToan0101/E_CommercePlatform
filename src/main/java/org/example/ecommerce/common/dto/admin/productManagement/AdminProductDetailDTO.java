package org.example.ecommerce.common.dto.admin.productManagement;

import java.time.Instant;
import java.util.List;

/**
 * DTO thể hiện chi tiết sản phẩm cho Admin
 */
public class AdminProductDetailDTO {
    private Integer id;
    private String name;
    private String shopName;
    private String categoryName;
    private String description;
    private String status;
    private Instant createdAt;
    private List<InventoryDTO> inventories;
    private List<String> images;

    public AdminProductDetailDTO(
            Integer id,
            String name,
            String shopName,
            String categoryName,
            String description,
            String status,
            Instant createdAt,
            List<InventoryDTO> inventories,
            List<String> images
    ) {
        this.id = id;
        this.name = name;
        this.shopName = shopName;
        this.categoryName = categoryName;
        this.description = description;
        this.status = status;
        this.createdAt = createdAt;
        this.inventories = inventories;
        this.images = images;
    }

    // --- getters & setters ---
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public List<InventoryDTO> getInventories() {
        return inventories;
    }

    public void setInventories(List<InventoryDTO> inventories) {
        this.inventories = inventories;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }
}
