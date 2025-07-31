package org.example.ecommerce.service.customer.customer_product;

import org.example.ecommerce.entity.*;
import org.example.ecommerce.repository.*;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CustomerProductServiceImpl implements CustomerProductService {
    @Autowired
    private final ProductRepository productRepository;
    @Autowired
    private final ProductimageRepository productimageRepository;
    @Autowired
    private final InventoryRepository   inventoryRepository;
    @Autowired
    private final ShopRepository shopRepository;
    @Autowired
    private final ReviewRepository reviewRepository;
    @Autowired
    private final CategoryRepository categoryRepository;

    public CustomerProductServiceImpl(ProductRepository productRepository, ProductimageRepository productimageRepository, InventoryRepository inventoryRepository, ShopRepository shopRepository, ReviewRepository reviewRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.productimageRepository = productimageRepository;
        this.inventoryRepository = inventoryRepository;
        this.shopRepository = shopRepository;
        this.reviewRepository = reviewRepository;
        this.categoryRepository = categoryRepository;
    }

    public List<ProductView> getProductViews() {
        List<Object[]> results = productRepository.findAvailableProductsOptimized();
        List<ProductView> views = new ArrayList<>();
        
        for (Object[] row : results) {
            Integer productId = (Integer) row[0];
            String productName = (String) row[1];
            String fullAddress = (String) row[4];
            BigDecimal price = (BigDecimal) row[5];
            Long solditems = (Long) row[6];
            
            // Process shop address
            String shopaddress = "";
            if (fullAddress != null) {
                int index = fullAddress.lastIndexOf("-");
                shopaddress = (index != -1) ? fullAddress.substring(index + 1).trim() : fullAddress;
            }
            
            // Handle null values
            if (price == null) price = BigDecimal.ZERO;
            if (solditems == null) solditems = 0L;
            
            // Get image URL separately
            String imageUrl = productimageRepository.findFirstImageUrlByProductId(productId);
            
            views.add(new ProductView(productId, productName, price, imageUrl, shopaddress, 0.0f, solditems.intValue()));
        }
        
        return views;
    }
    


    public List<Category> getCategories() {


        return categoryRepository.findRootCategories().stream().filter(category -> category.getId() != 313).toList();
    }

    public Shop getShops(Integer shopid) {
        return shopRepository.findShopsById(shopid);
    }

    public List<ProductView> getProductViewsByShopId(Integer shopid) {
        List<Object[]> results = productRepository.findAvailableProductsByShopIdOptimized(shopid);
        List<ProductView> views = new ArrayList<>();
        
        for (Object[] row : results) {
            Integer productId = (Integer) row[0];
            String productName = (String) row[1];
            String fullAddress = (String) row[4];
            BigDecimal price = (BigDecimal) row[5];
            Long solditems = (Long) row[6];
            
            // Process shop address
            String shopaddress = "";
            if (fullAddress != null) {
                int index = fullAddress.lastIndexOf("-");
                shopaddress = (index != -1) ? fullAddress.substring(index + 1).trim() : fullAddress;
            }
            
            // Handle null values
            if (price == null) price = BigDecimal.ZERO;
            if (solditems == null) solditems = 0L;
            
            // Get image URL separately
            String imageUrl = productimageRepository.findFirstImageUrlByProductId(productId);
            
            views.add(new ProductView(productId, productName, price, imageUrl, shopaddress, 0.0f, solditems.intValue()));
        }
        
        return views;
    }
}
