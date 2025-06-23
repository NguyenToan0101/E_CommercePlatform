package org.example.ecommerce.service.customer.customer_product;

import org.example.ecommerce.entity.Inventory;
import org.example.ecommerce.entity.Product;
import org.example.ecommerce.entity.Productimage;
import org.example.ecommerce.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
        List<Product> products = productRepository.findAll();
        List<ProductView> views = new ArrayList<>();
        for (Product p : products) {
            if (p.getStatus().equals("active") && inventoryRepository.findInventoriesById(p.getId()).getQuantity()>0) {
                int totalSold = inventoryRepository.findInventoryById(p.getId())
                        .stream()
                        .mapToInt(Inventory::getSolditems)
                        .sum();

                List<Productimage> imgs = productimageRepository.findProductimageById(p.getId());
                String imageUrl = imgs.isEmpty() ? null : imgs.get(0).getImageurl();

                String fullAddress = shopRepository.findById(p.getShopid().getId()).get().getFulladdress();
                String shopaddress = fullAddress.substring(fullAddress.lastIndexOf(",") + 1).trim();

                List<Integer> rates = reviewRepository.findRateById(p.getId());
                float rate = 0f;
                if (!rates.isEmpty()) {
                    float sum = 0f;
                    for (int r : rates) {
                        sum += r;
                    }
                    rate = Math.round((sum / rates.size()) * 10f) / 10f;
                }

                Integer categoryId = null;
                String categoryName = null;
                if (p.getCategoryid() != null) {
                    categoryId = p.getCategoryid().getId();
                    categoryName= categoryRepository.findById(categoryId).get().getCategoryname();
                }

                views.add(new ProductView(p.getId(), p.getName(), p.getPrice(), totalSold, imageUrl, shopaddress, rate, categoryId, categoryName));
            }
        }
        return views;
    }

}
