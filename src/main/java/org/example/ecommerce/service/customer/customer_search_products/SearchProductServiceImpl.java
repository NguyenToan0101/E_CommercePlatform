package org.example.ecommerce.service.customer.customer_search_products;

import org.example.ecommerce.entity.Inventory;
import org.example.ecommerce.entity.Product;
import org.example.ecommerce.entity.Productimage;
import org.example.ecommerce.repository.*;
import org.example.ecommerce.service.customer.customer_product.ProductView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SearchProductServiceImpl implements SearchProductService {
    @Autowired
    private final CategoryRepository categoryRepository;
    @Autowired
    private final ProductRepository productRepository;
    @Autowired
    private final ProductimageRepository productimageRepository;
    @Autowired
    private final InventoryRepository inventoryRepository;
    @Autowired
    private final ShopRepository shopRepository;
    @Autowired
    private final ReviewRepository reviewRepository;

    public SearchProductServiceImpl(CategoryRepository categoryRepository, ProductRepository productRepository, ProductimageRepository productimageRepository, InventoryRepository inventoryRepository, ShopRepository shopRepository, ReviewRepository reviewRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.productimageRepository = productimageRepository;
        this.inventoryRepository = inventoryRepository;
        this.shopRepository = shopRepository;
        this.reviewRepository = reviewRepository;
    }

    @Override
    public List<ProductView> getProductCategory(Integer categoryId) {
        List<ProductView> views = new ArrayList<>();

        List<Product> products = productRepository.findAll();

        for (Product p : products) {
            if (p.getStatus().equals("active") && p.getCategoryid() != null && p.getCategoryid().getId().equals(categoryId) && inventoryRepository.findInventoriesById(p.getId()).getQuantity()>0) {

                int totalSold = inventoryRepository.findInventoryByProductid(p)
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

                String categoryName = categoryRepository.findById(categoryId).get().getCategoryname();

                views.add(new ProductView(p.getId(), p.getName(), p.getPrice(), totalSold, imageUrl, shopaddress, rate, categoryId, categoryName
                ));
            }
        }

        return views;
    }

    public List<ProductView> searchByName(String keyword) {
        List<Product> products = productRepository.findByNameContainingIgnoreCase(keyword);
        List<ProductView> views = new ArrayList<>();
        for (Product p : products) {
            if (p.getStatus().equals("active")){
                int totalSold = inventoryRepository.findInventoryByProductid(p)
                        .stream()
                        .mapToInt(i -> i.getSolditems())
                        .sum();

                List<Productimage> imgs = productimageRepository.findProductimageById(p.getId());
                String imageUrl = imgs.isEmpty() ? null : imgs.get(0).getImageurl();

                String fullAddress = shopRepository.findById(p.getShopid().getId()).get().getFulladdress();
                String shopaddress = fullAddress.substring(fullAddress.lastIndexOf(",") + 1).trim();

                List<Integer> rates = reviewRepository.findRateById(p.getId());
                float rate = 0f;
                if (!rates.isEmpty()) {
                    float sum = 0f;
                    for (int r : rates) sum += r;
                    rate = Math.round((sum / rates.size()) * 10f) / 10f;
                }

                Integer categoryId = null;
                String categoryName = null;
                if (p.getCategoryid() != null) {
                    categoryId = p.getCategoryid().getId();
                    categoryName = categoryRepository.findById(categoryId).get().getCategoryname();
                }

                views.add(new ProductView(p.getId(), p.getName(), p.getPrice(), totalSold, imageUrl, shopaddress, rate, categoryId, categoryName));
            }
        }
        return views;
    }
}
