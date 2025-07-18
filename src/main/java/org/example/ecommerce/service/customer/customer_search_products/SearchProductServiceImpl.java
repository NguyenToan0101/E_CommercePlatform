package org.example.ecommerce.service.customer.customer_search_products;

import org.example.ecommerce.entity.Inventory;
import org.example.ecommerce.entity.Product;
import org.example.ecommerce.entity.Productimage;
import org.example.ecommerce.entity.Category;
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

        // Lấy tất cả category con (bao gồm cả chính nó)
        List<Integer> allCategoryIds = new ArrayList<>();
        collectCategoryAndChildren(categoryId, allCategoryIds);

        List<Product> products = productRepository.findAll();

        for (Product p : products) {
            if (p.getStatus().equals("available") && p.getCategoryid() != null && allCategoryIds.contains(p.getCategoryid().getId())) {
                List<Productimage> imgs = productimageRepository.findAllByProductid(p);
                String imageUrl = imgs.isEmpty() ? null : imgs.get(0).getImageurl();

                String fullAddress = shopRepository.findById(p.getShopid().getId()).get().getFulladdress();
                String keyword = "Tỉnh";
                int index = fullAddress.indexOf(keyword);
                String shopaddress = (index != -1) ? fullAddress.substring(index + keyword.length()).trim() : fullAddress;

                Float avgRating = reviewRepository.findAverageRatingByProductid(p);
                float rate = (avgRating != null) ? avgRating : 0f;

                Integer sumSold = inventoryRepository.findSumsolditemsByProductid(p);
                int solditems = (sumSold != null) ? sumSold : 0;

                String categoryName = categoryRepository.findById(p.getCategoryid().getId()).get().getCategoryname();

                views.add(new ProductView(p.getId(), p.getName(), (inventoryRepository.findFirstByProductidOrderByPriceAsc(p).getPrice()), imageUrl, shopaddress, rate, p.getCategoryid().getId(), categoryName, solditems));
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
            if (p.getStatus().equals("available")){

                List<Productimage> imgs = productimageRepository.findAllByProductid(p);
                String imageUrl = imgs.isEmpty() ? null : imgs.get(0).getImageurl();

                String fullAddress = shopRepository.findById(p.getShopid().getId()).get().getFulladdress();
                String key = "Tỉnh";
                int index = fullAddress.indexOf(key);
                String shopaddress = (index != -1) ? fullAddress.substring(index + key.length()).trim() : fullAddress;


                Float avgRating = reviewRepository.findAverageRatingByProductid(p);
                float rate = (avgRating != null) ? avgRating : 0f;

                Integer sumSold = inventoryRepository.findSumsolditemsByProductid(p);
                int solditems = (sumSold != null) ? sumSold : 0;



                Integer categoryId = null;
                String categoryName = null;
                if (p.getCategoryid() != null) {
                    categoryId = p.getCategoryid().getId();
                    categoryName = categoryRepository.findById(categoryId).get().getCategoryname();
                }

                views.add(new ProductView(p.getId(), p.getName(), (inventoryRepository.findFirstByProductidOrderByPriceAsc(p).getPrice()), imageUrl, shopaddress, rate, categoryId, categoryName, solditems));
            }
        }
        return views;
    }
}
