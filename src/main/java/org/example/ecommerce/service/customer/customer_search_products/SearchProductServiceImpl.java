package org.example.ecommerce.service.customer.customer_search_products;

import org.example.ecommerce.entity.*;
import org.example.ecommerce.repository.*;
import org.example.ecommerce.service.customer.customer_product.ProductView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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

        // Lấy tất cả category con (bao gồm cả chính nó)
        List<Integer> allCategoryIds = new ArrayList<>();
        collectCategoryAndChildren(categoryId, allCategoryIds);

        List<Product> products = productRepository.findAll();

        for (Product p : products) {
            if (p.getStatus().equals("available") && allCategoryIds.contains(p.getCategoryid().getId())) {

                String imageUrl = productimageRepository.findFirstImageUrlByProductId(p.getId());

                String fullAddress = p.getShopid().getFulladdress();
                int index = fullAddress.lastIndexOf("-");
                String shopaddress = (index != -1) ? fullAddress.substring(index + 1).trim() : fullAddress;



                float rate = (float) p.getReviews().stream().mapToDouble(Review::getRating).average().orElse(0);

                int solditems = p.getInventoriesView().stream().mapToInt(Inventory::getSolditems).sum();

                BigDecimal price = p.getInventoriesView().stream().map(Inventory::getPrice).min(BigDecimal::compareTo).orElse(BigDecimal.ZERO);

                views.add(new ProductView(p.getId(), p.getName(), price, imageUrl, shopaddress, rate, solditems));
            }
        }
        return views;
    }

    // Đệ quy lấy tất cả id category con
    private void collectCategoryAndChildren(Integer parentId, List<Integer> result) {
        result.add(parentId);
        List<Category> children = categoryRepository.findByParent_Id(parentId);
        for (Category child : children) {
            collectCategoryAndChildren(child.getId(), result);
        }
    }

    public List<ProductView> searchByName(String keyword) {
        List<Product> products = productRepository.findByNameContainingIgnoreCase(keyword);
        List<ProductView> views = new ArrayList<>();
        for (Product p : products) {
            if (p.getStatus().equals("available")) {

                String imageUrl = productimageRepository.findFirstImageUrlByProductId(p.getId());

                String fullAddress = p.getShopid().getFulladdress();
                int index = fullAddress.lastIndexOf("-");
                String shopaddress = (index != -1) ? fullAddress.substring(index + 1).trim() : fullAddress;



                float rate = (float) p.getReviews().stream().mapToDouble(Review::getRating).average().orElse(0);

                int solditems = p.getInventoriesView().stream().mapToInt(Inventory::getSolditems).sum();

                BigDecimal price = p.getInventoriesView().stream().map(Inventory::getPrice).min(BigDecimal::compareTo).orElse(BigDecimal.ZERO);

                views.add(new ProductView(p.getId(), p.getName(), price, imageUrl, shopaddress, rate, solditems));
            }
        }
        return views;
    }

    public List<ProductView> searchByPriceAndRate(BigDecimal priceMin, BigDecimal priceMax, Integer rates, String keyword) {
        List<Product> products = productRepository.findByNameContainingIgnoreCase(keyword);
        List<ProductView> views = new ArrayList<>();
        for (Product p : products) {
            if (p.getStatus().equals("available")) {

                String imageUrl = productimageRepository.findFirstImageUrlByProductId(p.getId());

                String fullAddress = p.getShopid().getFulladdress();
                int index = fullAddress.lastIndexOf("-");
                String shopaddress = (index != -1) ? fullAddress.substring(index + 1).trim() : fullAddress;


                float rate = (float) p.getReviews().stream().mapToDouble(Review::getRating).average().orElse(0);

                if(rate<rates){
                    continue;
                }

                int solditems = p.getInventoriesView().stream().mapToInt(Inventory::getSolditems).sum();

                List<Inventory> inventories = p.getInventories().stream()
                        .filter(i -> i.getPrice().compareTo(priceMin) >= 0 && i.getPrice().compareTo(priceMax) <= 0)
                        .toList();

                if (inventories.isEmpty()) {
                    continue;
                }

                BigDecimal price = inventories.stream().map(Inventory::getPrice).min(BigDecimal::compareTo).orElse(BigDecimal.ZERO);

                views.add(new ProductView(p.getId(), p.getName(),price , imageUrl, shopaddress, rate, solditems));
            }
        }
        return views;
    }

    public List<ProductView> searchCategoryByPriceAndRate(BigDecimal priceMin, BigDecimal priceMax, Integer rates, Integer categoryId) {
        List<ProductView> views = new ArrayList<>();

        // Lấy tất cả category con (bao gồm cả chính nó)
        List<Integer> allCategoryIds = new ArrayList<>();
        collectCategoryAndChildren(categoryId, allCategoryIds);

        List<Product> products = productRepository.findAll();
        for (Product p : products) {
            if (p.getStatus().equals("available")&& allCategoryIds.contains(p.getCategoryid().getId())) {

                String imageUrl = productimageRepository.findFirstImageUrlByProductId(p.getId());

                String fullAddress = p.getShopid().getFulladdress();
                int index = fullAddress.lastIndexOf("-");
                String shopaddress = (index != -1) ? fullAddress.substring(index + 1).trim() : fullAddress;


                float rate = (float) p.getReviews().stream().mapToDouble(Review::getRating).average().orElse(0);

                if(rate<rates){
                    continue;
                }

                int solditems = p.getInventoriesView().stream().mapToInt(Inventory::getSolditems).sum();

                List<Inventory> inventories = p.getInventories().stream()
                        .filter(i -> i.getPrice().compareTo(priceMin) >= 0 && i.getPrice().compareTo(priceMax) <= 0)
                        .toList();

                if (inventories.isEmpty()) {
                    continue;
                }

                BigDecimal price = inventories.stream().map(Inventory::getPrice).min(BigDecimal::compareTo).orElse(BigDecimal.ZERO);

                views.add(new ProductView(p.getId(), p.getName(),price , imageUrl, shopaddress, rate, solditems));
            }
        }
        return views;
    }
}