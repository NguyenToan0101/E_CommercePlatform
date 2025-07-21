package org.example.ecommerce.service.customer.customer_product;

import org.example.ecommerce.entity.*;
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
            if (p.getStatus().equals("available")) {

                List<Productimage> imgs = productimageRepository.findAllByProductid(p);
                String imageUrl = imgs.isEmpty() ? null : imgs.get(0).getImageurl();

                String fullAddress = shopRepository.findById(p.getShopid().getId()).get().getFulladdress();
                int index = fullAddress.lastIndexOf("-");
                String shopaddress = (index != -1) ? fullAddress.substring(index + 1).trim() : fullAddress;


                Float avgRating = reviewRepository.findAverageRatingByProductid(p);
                float rate = (avgRating != null) ? avgRating : 0f;

                Integer sumSold = inventoryRepository.findSumsolditemsByProductid(p);
                int solditems = (sumSold != null) ? sumSold : 0;


                Integer categoryId = null;
                String categoryName = null;
                if (p.getCategoryid() != null) {
                    categoryId = p.getCategoryid().getId();
                    categoryName= categoryRepository.findById(categoryId).get().getCategoryname();
                }

                views.add(new ProductView(p.getId(), p.getName(), (inventoryRepository.findFirstByProductidOrderByPriceAsc(p).getPrice()), imageUrl, shopaddress, rate, categoryId, categoryName,solditems));
            }
        }
        return views;
    }

    public List<Category> getCategories() {
        return categoryRepository.findRootCategories();
    }

    public Shop getShops(Integer shopid) {
        return shopRepository.findShopsById(shopid);
    }

    public List<ProductView> getProductViewsByShopId(Integer shopid) {
        List<Product> products = productRepository.findAllByShopid_Id(shopid);
        List<ProductView> views = new ArrayList<>();
        for (Product p : products) {
            if (p.getStatus().equals("available")) {

                List<Productimage> imgs = productimageRepository.findAllByProductid(p);
                String imageUrl = imgs.isEmpty() ? null : imgs.get(0).getImageurl();

                String fullAddress = shopRepository.findById(p.getShopid().getId()).get().getFulladdress();
                int index = fullAddress.lastIndexOf("-");
                String shopaddress = (index != -1) ? fullAddress.substring(index + 1).trim() : fullAddress;


                Float avgRating = reviewRepository.findAverageRatingByProductid(p);
                float rate = (avgRating != null) ? avgRating : 0f;

                Integer sumSold = inventoryRepository.findSumsolditemsByProductid(p);
                int solditems = (sumSold != null) ? sumSold : 0;


                Integer categoryId = null;
                String categoryName = null;
                if (p.getCategoryid() != null) {
                    categoryId = p.getCategoryid().getId();
                    categoryName= categoryRepository.findById(categoryId).get().getCategoryname();
                }

                views.add(new ProductView(p.getId(), p.getName(), (inventoryRepository.findFirstByProductidOrderByPriceAsc(p).getPrice()), imageUrl, shopaddress, rate, categoryId, categoryName,solditems));
            }
        }
        return views;
    }
}
