package org.example.ecommerce.service.seller.promotion;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.ecommerce.dto.promotion.CreatePromotionRequest;
import org.example.ecommerce.entity.*;
import org.example.ecommerce.repository.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PromotionSellerService {

    private final PromotionRepository promotionRepository;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ShopRepository shopRepository;
    private final PromotionTargetRepository promotionTargetRepository;
    private final PromotionCategoryRepository promotionCategoryRepository;

    @Transactional
    public Promotion createPromotion(CreatePromotionRequest request, Shop shopId) {
        // Validate shop exists
        Shop shop = shopRepository.findById(shopId.getId())
                .orElseThrow(() -> new IllegalArgumentException("Shop not found with id: " + shopId));

        // Create and save promotion
        Promotion promotion = new Promotion();
        promotion.setCode(request.getCode());
        promotion.setName(request.getName());
        promotion.setDescription(request.getDescription());
        promotion.setValue(request.getValue());
        promotion.setType(request.getType());
        promotion.setStartdate(request.getStartDate());
        promotion.setEnddate(request.getEndDate());
        promotion.setMinOrderValue(request.getMinOrderValue());
        promotion.setMaxDiscount(request.getMaxDiscount());
        promotion.setUsageLimit(request.getUsageLimit());
        // Mặc định mỗi người dùng chỉ được sử dụng 1 lần
        promotion.setPerUserLimit(1);
        promotion.setStatus("ACTIVE");
        promotion.setShop(shop);
        
        // Save promotion first to get the ID
        Promotion savedPromotion = promotionRepository.save(promotion);

        // Handle category-based promotion
        if (request.getCategoryIds() != null && !request.getCategoryIds().isEmpty()) {
            List<Category> categories = categoryRepository.findAllById(request.getCategoryIds());
            
            // Save promotion-category relationships
            categories.forEach(category -> {
                PromotionCategory pc = new PromotionCategory();
                pc.setPromotionid(savedPromotion);
                pc.setCategoryid(category);
                promotionCategoryRepository.save(pc);
            });
            
            // Get all products from these categories in the shop and create promotion targets
            List<Product> products = productRepository.findByShopidAndCategoryidIn(shopId, (List)request.getCategoryIds());
            createPromotionTargets(savedPromotion, products);
        }
        
        // Handle product-specific promotion
        if (request.getProductIds() != null && !request.getProductIds().isEmpty()) {
            List<Product> products = productRepository.findAllById(request.getProductIds())
                    .stream()
                    .filter(p -> p.getShopid().getId().equals(shopId)) // Ensure products belong to the shop
                    .collect(Collectors.toList());
                    
            createPromotionTargets(savedPromotion, products);
        }

        return savedPromotion;
    }
    
    private void createPromotionTargets(Promotion promotion, List<Product> products) {
        products.forEach(product -> {
            PromotionTarget target = new PromotionTarget();
            target.setPromotionid(promotion);
            target.setProductid(product);
            target.setShopid(product.getShopid());
            promotionTargetRepository.save(target);
        });
    }
    

    public List<Promotion> getPromotionsByShop(Shop shop) {
        return promotionRepository.findByShop(shop);
    }
}
