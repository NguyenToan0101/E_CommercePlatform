package org.example.ecommerce.service.seller;

import org.example.ecommerce.common.dto.ProductSalesDTO;
import org.example.ecommerce.common.dto.InventorySalesDTO;
import org.example.ecommerce.common.dto.ProductSalesDTO;
import org.example.ecommerce.entity.*;
import org.example.ecommerce.repository.CategoryRepository;
import org.example.ecommerce.repository.OrderItemsRepository;
import org.example.ecommerce.repository.ProductRepository;
import org.example.ecommerce.repository.ProductimageRepository;
import org.example.ecommerce.repository.ShopRepository;
import org.example.ecommerce.service.UploadImageFile;
import org.example.ecommerce.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired private ProductRepository productRepository;
    @Autowired private ProductimageRepository imageRepository;
    @Autowired private UploadImageFile uploadImageFile;
    @Autowired private ShopRepository shopRepository;
    @Autowired private CategoryRepository categoryRepository;
    @Autowired private OrderItemsRepository orderItemsRepository;
    @Autowired private CategoryService categoryService;


    public Product getById(Integer id) {
        return productRepository.findById(id).orElse(null);
    }

    public void save(Product product, MultipartFile[] images, MultipartFile[] inventoryImages, Integer shopId) {

        if (product.getDescription() != null) {
            product.setDescription(product.getDescription().replaceAll("<[^>]*>", ""));
        }

        if (product.getStatus() == null || product.getStatus().isEmpty()) {
            product.setStatus("pending_approval");
        }

        if (product.getCreatedat() == null) {
            product.setCreatedat(Instant.now());
        }

        if (product.getCategoryid() != null) {
            Category category = categoryRepository.findById(product.getCategoryid().getId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            product.setCategoryid(category);
        }

        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new RuntimeException("Shop không tồn tại"));
        product.setShopid(shop);

        // Chỉ lưu các biến thể do người dùng nhập
        if (product.getInventories() != null) {
            int imgIdx = 0;
            for (int i = 0; i < product.getInventories().size(); i++) {
                Inventory inventory = product.getInventories().get(i);
                if (inventory.getQuantity() == null || inventory.getQuantity() <= 0) {
                    continue;
                }
                inventory.setProductid(product);
                inventory.setUpdatedAt(Instant.now());
                // Mapping file inventoryImages theo thứ tự thực tế
                if (inventoryImages != null && inventoryImages.length > imgIdx && !inventoryImages[imgIdx].isEmpty()) {
                    try {
                        String imageUrl = uploadImageFile.uploadImage(inventoryImages[imgIdx]);
                        inventory.setImage(imageUrl);
                    } catch (IOException e) {
                        throw new RuntimeException("Lỗi upload ảnh inventory: " + e.getMessage());
                    }
                }
                imgIdx++;
            }
        }

        Product saved = productRepository.save(product);

        // Lưu ảnh sản phẩm tổng
        for (MultipartFile image : images) {
            if (!image.isEmpty()) {
                try {
                    String imageUrl = uploadImageFile.uploadImage(image);
                    Productimage img = new Productimage();
                    img.setProductid(saved);
                    img.setImageurl(imageUrl);
                    imageRepository.save(img);
                } catch (IOException e) {
                    throw new RuntimeException("Lỗi upload ảnh: " + e.getMessage());
                }
            }
        }
    }

    public void updateProduct(Integer id, Product updatedProduct, MultipartFile[] images, MultipartFile[] inventoryImages) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Làm sạch mô tả
        if (updatedProduct.getDescription() != null) {
            updatedProduct.setDescription(updatedProduct.getDescription().replaceAll("<[^>]*>", ""));
        }

        existingProduct.setName(updatedProduct.getName());
        existingProduct.setDescription(updatedProduct.getDescription());
        existingProduct.setStatus(updatedProduct.getStatus());
        existingProduct.setCreatedat(Instant.now());

        // Cập nhật danh mục nếu có
        if (updatedProduct.getCategoryid() != null) {
            Category category = categoryRepository.findById(updatedProduct.getCategoryid().getId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            existingProduct.setCategoryid(category);
        }

        // Chỉ lưu các biến thể do người dùng nhập
        if (updatedProduct.getInventories() != null) {
            existingProduct.getInventories().clear();
            int imgIdx = 0;
            for (int i = 0; i < updatedProduct.getInventories().size(); i++) {
                Inventory inventory = updatedProduct.getInventories().get(i);
                if (inventory.getQuantity() == null || inventory.getQuantity() <= 0) {
                    continue;
                }
                inventory.setProductid(existingProduct);
                inventory.setUpdatedAt(Instant.now());
                if (inventoryImages != null && inventoryImages.length > imgIdx && !inventoryImages[imgIdx].isEmpty()) {
                    try {
                        String imageUrl = uploadImageFile.uploadImage(inventoryImages[imgIdx]);
                        inventory.setImage(imageUrl);
                    } catch (IOException e) {
                        throw new RuntimeException("Lỗi upload ảnh inventory: " + e.getMessage());
                    }
                }
                existingProduct.getInventories().add(inventory);
                imgIdx++;
            }
        }

        Product saved = productRepository.save(existingProduct);

        // Lưu ảnh mới nếu có
        for (MultipartFile image : images) {
            if (!image.isEmpty()) {
                try {
                    String imageUrl = uploadImageFile.uploadImage(image);
                    Productimage img = new Productimage();
                    img.setProductid(saved);
                    img.setImageurl(imageUrl);
                    imageRepository.save(img);
                } catch (IOException e) {
                    throw new RuntimeException("Lỗi upload ảnh: " + e.getMessage());
                }
            }
        }
    }


    public void softDeleteProducts(List<Integer> ids) {
        List<Product> products = productRepository.findAllById(ids);
        for (Product product : products) {
            product.setStatus("INACTIVE");
        }
        productRepository.saveAll(products);
    }


    public void showProducts(List<Integer> ids) {
        List<Product> products = productRepository.findAllById(ids);
        for (Product product : products) {
            product.setStatus("available");
        }
        productRepository.saveAll(products);
    }

    public Map<String, Long> getStatusCounts(Integer shopId) {
        Map<String, Long> counts = new HashMap<>();
        List<Object[]> results = productRepository.countProductsByStatus(shopId);
        for (Object[] result : results) {
            if (result[0] instanceof String && result[1] instanceof Long) {
                counts.put((String) result[0], (Long) result[1]);
            }
        }
        counts.putIfAbsent("available", 0L);
        counts.putIfAbsent("INACTIVE", 0L);
        counts.putIfAbsent("violation", 0L);
        counts.putIfAbsent("pending_review", 0L);
        return counts;
    }

    public Long getTotalProductCount(Integer shopId) {
        return productRepository.countByShopidId(shopId);
    }

    public Page<ProductSalesDTO> getProductSalesData(Integer shopId, String status, String keyword, Integer categoryId, Pageable pageable) {
        List<Product> products;
        if (status != null && !status.isEmpty() && !status.equalsIgnoreCase("all")) {
            products = productRepository.findByShopidIdAndStatus(shopId, status);
        } else {
            products = productRepository.findByShopidId(shopId);
        }
        // Filter by keyword
        if (keyword != null && !keyword.isEmpty()) {
            String lowerCaseKeyword = keyword.toLowerCase();
            products = products.stream()
                    .filter(p -> p.getName().toLowerCase().contains(lowerCaseKeyword))
                    .collect(Collectors.toList());
        }
        // Filter by category
        if (categoryId != null) {
            products = products.stream()
                    .filter(p -> p.getCategoryid() != null && categoryService.isCategoryOrSubcategory(categoryId, p.getCategoryid().getId()))
                    .collect(Collectors.toList());
        }
        List<ProductSalesDTO> dtos = products.stream().map(this::mapToProductSalesDTO).collect(Collectors.toList());
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), dtos.size());
        List<ProductSalesDTO> pageContent = (start <= end) ? dtos.subList(start, end) : List.of();
        return new PageImpl<>(pageContent, pageable, dtos.size());
    }

    private ProductSalesDTO mapToProductSalesDTO(Product product) {
        List<Orderitem> orderItems = orderItemsRepository.findByProductid(product);

        BigDecimal totalRevenue = orderItems.stream()
                .map(item -> item.getUnitprice().multiply(new BigDecimal(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        int totalSoldQuantity = orderItems.stream()
                .mapToInt(Orderitem::getQuantity)
                .sum();

        // Loại bỏ inventory trùng (theo id)
        List<Inventory> uniqueInventories = product.getInventories().stream()
                .filter(Objects::nonNull)
                .collect(Collectors.collectingAndThen(
                        Collectors.toMap(Inventory::getId, inv -> inv, (inv1, inv2) -> inv1),
                        m -> m.values().stream().collect(Collectors.toList())
                ));

        int totalStockQuantity = uniqueInventories.stream()
                .mapToInt(inv -> inv.getQuantity() != null ? inv.getQuantity() : 0)
                .sum();

        List<BigDecimal> prices = uniqueInventories.stream()
                .map(Inventory::getPrice)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        BigDecimal minPrice = prices.stream().min(BigDecimal::compareTo).orElse(BigDecimal.ZERO);
        BigDecimal maxPrice = prices.stream().max(BigDecimal::compareTo).orElse(BigDecimal.ZERO);
        String priceRange = minPrice.compareTo(maxPrice) == 0 ?
                String.format("%,.0fđ", minPrice) :
                String.format("%,.0fđ - %,.0fđ", minPrice, maxPrice);

        List<InventorySalesDTO> inventorySales = uniqueInventories.stream()
                .map(inventory -> {
                    List<Orderitem> itemsForInventory = orderItems.stream()
                            .filter(oi -> oi.getInventoryid() != null && oi.getInventoryid().equals(inventory))
                            .collect(Collectors.toList());

                    int soldQuantity = itemsForInventory.stream().mapToInt(Orderitem::getQuantity).sum();
                    BigDecimal revenue = itemsForInventory.stream()
                            .map(item -> item.getUnitprice().multiply(new BigDecimal(item.getQuantity())))
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    return new InventorySalesDTO(
                            inventory.getId(),
                            inventory.getColor(),
                            inventory.getDimension(),
                            inventory.getQuantity(), // Tồn kho
                            soldQuantity,           // Đã bán
                            revenue,
                            inventory.getPrice()
                    );
                }).collect(Collectors.toList());

        String imageUrl = product.getProductimages().stream()
                .map(Productimage::getImageurl)
                .findFirst()
                .orElse(""); // Trả về chuỗi rỗng nếu không có ảnh

        Integer categoryId = product.getCategoryid() != null ? product.getCategoryid().getId() : null;
        String categoryName = product.getCategoryid() != null ? product.getCategoryid().getCategoryname() : "Chưa phân loại";

        return new ProductSalesDTO(
                product.getId(),
                product.getName(),
                product.getStatus(),
                imageUrl,
                categoryId,
                categoryName,
                priceRange,
                totalRevenue,
                totalSoldQuantity,
                totalStockQuantity,
                inventorySales
        );
    }
}

