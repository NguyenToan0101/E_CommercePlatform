package org.example.ecommerce.service.seller;

import org.example.ecommerce.common.dto.seller.InventorySalesDTO;
import org.example.ecommerce.common.dto.seller.ProductSalesDTO;
import org.example.ecommerce.entity.*;
import org.example.ecommerce.repository.CategoryRepository;
import org.example.ecommerce.repository.OrderItemsRepository;
import org.example.ecommerce.repository.ProductRepository;
import org.example.ecommerce.repository.ProductimageRepository;
import org.example.ecommerce.repository.ShopRepository;
import org.example.ecommerce.repository.InventoryRepository;
import org.example.ecommerce.service.UploadImageFile;
import org.example.ecommerce.service.CategoryService;

//import org.example.ecommerce.service.customer.customer_search_products.SearchProductServiceImpl;
//import org.example.ecommerce.service.customer.search.ProductDocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
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
    @Autowired private InventoryRepository inventoryRepository;
    @Autowired private CategoryService categoryService;

//    private final ProductDocumentService productDocumentService;
//    public ProductService( ProductDocumentService productDocumentService) {
//        this.productDocumentService = productDocumentService;
//
//    }


    public Product getById(Integer id) {
        return productRepository.findById(id).orElse(null);
    }

    public void save(Product product, MultipartFile[] images, String[] colorNames, MultipartFile[] colorImages, Integer shopId) {
        handleProductSaving(product, images, colorNames, colorImages, shopId, null);
    }

    public void updateProduct(Integer id, Product updatedProduct, MultipartFile[] images, String[] colorNames, MultipartFile[] colorImages) {
        // Kiểm tra sản phẩm đã từng có order chưa
        Product productInDb = productRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Product not found"));
        boolean hasOrder = orderItemsRepository.findByProductid(productInDb).size() > 0;
        if (hasOrder) {
            throw new IllegalStateException("Không thể chỉnh sửa sản phẩm đã có người mua!");
        }
        handleProductSaving(updatedProduct, images, colorNames, colorImages, null, id);
    }
    private void handleProductSaving(Product product, MultipartFile[] images, String[] colorNames, MultipartFile[] colorImages, Integer shopId, Integer productId) {
        Product targetProduct;
        if (productId != null) { // Update
            targetProduct = productRepository.findById(productId)
                    .orElseThrow(() -> new RuntimeException("Product not found"));
            targetProduct.getInventories().clear(); // Clear old inventories
        } else { // Create
            targetProduct = product;
        }

        if (product.getDescription() != null) {
            targetProduct.setDescription(product.getDescription().replaceAll("<[^>]*>", ""));
        }

        if (productId == null) {
            if (product.getStatus() == null || product.getStatus().isEmpty()) {
                targetProduct.setStatus("pending_approval");
            }
            targetProduct.setCreatedat(Instant.now());
        } else {
            targetProduct.setStatus(product.getStatus());
        }
        targetProduct.setName(product.getName());
        targetProduct.setUseVariantShipping(product.getUseVariantShipping());
        targetProduct.setWeight(product.getWeight());
        targetProduct.setLength(product.getLength());
        targetProduct.setWidth(product.getWidth());
        targetProduct.setHeight(product.getHeight());


        if (product.getCategoryid() != null) {
            Category category = categoryRepository.findById(product.getCategoryid().getId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            targetProduct.setCategoryid(category);
        }

        if (shopId != null) {
            Shop shop = shopRepository.findById(shopId)
                    .orElseThrow(() -> new RuntimeException("Shop không tồn tại"));
            targetProduct.setShopid(shop);
        }
        Map<String, String> colorImageUrlMap = new HashMap<>();
        if (colorNames != null && colorImages != null) {
            for (int i = 0; i < colorNames.length; i++) {
                if (!colorImages[i].isEmpty()) {
                    try {
                        String imageUrl = uploadImageFile.uploadImage(colorImages[i]);
                        colorImageUrlMap.put(colorNames[i], imageUrl);
                    } catch (IOException e) {
                        throw new RuntimeException("Lỗi upload ảnh màu: " + e.getMessage());
                    }
                }
            }
        }
        if (product.getInventories() != null) {
            List<Inventory> inventoriesCopy = new ArrayList<>(product.getInventories());
            for (Inventory inventory : inventoriesCopy) {
                if ((inventory.getQuantity() == null || inventory.getQuantity() <= 0) && inventory.getId() == null) {
                    continue;
                }
                if (productId == null) { // Chỉ set khi tạo mới
                    inventory.setSolditems(0);
                    if (inventory.getAlertQuantity() == null) {
                        inventory.setAlertQuantity(5);
                    }
                }
                inventory.setProductid(targetProduct);
                inventory.setUpdatedAt(Instant.now());
                if (inventory.getColor() != null && colorImageUrlMap.containsKey(inventory.getColor())) {
                    inventory.setImage(colorImageUrlMap.get(inventory.getColor()));
                }
                targetProduct.getInventories().add(inventory);
            }
        }

        Product saved = productRepository.save(targetProduct);

        if (images != null) {
            if (productId != null) {
                // Xóa toàn bộ ảnh cũ của sản phẩm khi update
                imageRepository.deleteAll(imageRepository.findAllByProductid(saved));
            }
            for (MultipartFile image : images) {
                if (image != null && !image.isEmpty()) {
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
    }


    public void softDeleteProducts(List<Integer> ids) {
        List<Product> products = productRepository.findAllById(ids);
        for (Product product : products) {
            if (!"available".equals(product.getStatus())) {
                throw new IllegalStateException("Chỉ có thể ẩn sản phẩm đang hoạt động!");
            }
            product.setStatus("hidding");
        }
        productRepository.saveAll(products);
    }


    public void showProducts(List<Integer> ids) {
        List<Product> products = productRepository.findAllById(ids);
        for (Product product : products) {
            if (!"hidding".equals(product.getStatus())) {
                throw new IllegalStateException("Chỉ có thể hiển thị sản phẩm đang ẩn!");
            }
            product.setStatus("available");
        }
        productRepository.saveAll(products);
    }

    public void updateInventoryAlertQuantity(Integer inventoryId, Integer alertQuantity) {
        Inventory inventory = null;
        for (Product product : productRepository.findAll()) {
            for (Inventory inv : product.getInventories()) {
                if (inv.getId().equals(inventoryId)) {
                    inventory = inv;
                    break;
                }
            }
            if (inventory != null) break;
        }
        if (inventory == null) throw new IllegalArgumentException("Không tìm thấy inventory với id: " + inventoryId);
        inventory.setAlertQuantity(alertQuantity);
        productRepository.save(inventory.getProductid());
    }

    public void updateInventoriesAlertQuantity(List<Integer> inventoryIds, List<Integer> alertQuantities) {
        if (inventoryIds.size() != alertQuantities.size()) {
            throw new IllegalArgumentException("Số lượng inventoryId và alertQuantity không khớp!");
        }
        for (int i = 0; i < inventoryIds.size(); i++) {
            updateInventoryAlertQuantity(inventoryIds.get(i), alertQuantities.get(i));
        }
    }

    public void updateAllInventoriesAlertQuantity(Integer productId, Integer alertQuantity) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy sản phẩm!"));
        for (Inventory inv : product.getInventories()) {
            inv.setAlertQuantity(alertQuantity);
        }
        productRepository.save(product);
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
        counts.putIfAbsent("hidding", 0L);
        counts.putIfAbsent("locked", 0L);
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
                            inventory.getPrice(),
                            inventory.getAlertQuantity(), // Thêm trường này
                            inventory.getImage() // imageUrl
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

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public List<Product> getAllProductsForShop(Integer shopId) {
        return productRepository.findByShopidId(shopId);
    }

    public void deleteProductHard(Integer id) {
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy sản phẩm!"));
        if (!"pending_approval".equals(product.getStatus())) {
            throw new IllegalStateException("Chỉ được xóa sản phẩm đang chờ duyệt!");
        }
        // Xóa ảnh sản phẩm
        imageRepository.deleteAll(imageRepository.findAllByProductid(product));
        // Xóa biến thể
        inventoryRepository.deleteAll(inventoryRepository.findAllByProductid(product));
        // Xóa sản phẩm
        productRepository.delete(product);
    }
}

