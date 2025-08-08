package org.example.ecommerce.service.customer.customer_product;

import org.example.ecommerce.entity.*;
import org.example.ecommerce.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
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
        List<Product> products = productRepository.findAll();
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

    public List<Category> getCategories() {
        List<Category> categoryList = new ArrayList<>();
        for (Category c : categoryRepository.findRootCategories()) {
            if (c.getId()==313){
                continue;
            }
            categoryList.add(c);
        }
        return categoryList;
    }

    public Shop getShops(Integer shopid) {
        return shopRepository.findShopsById(shopid);
    }

    public List<ProductView> getProductViewsByShopId(Integer shopid) {
        List<Product> products = productRepository.findAllByShopid_Id(shopid);
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
}