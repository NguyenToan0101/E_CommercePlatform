package org.example.ecommerce.service.customer.wishlist;

import org.example.ecommerce.entity.*;
import org.example.ecommerce.repository.*;
import org.example.ecommerce.service.customer.customer_product.ProductView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class WishlistServiceImpl implements WishlistService {
    @Autowired
    private WishlistRepository wishlistRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private ProductimageRepository productimageRepository;

    @Autowired
    private ShopRepository shopRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    public boolean toggleWishlist(Customer customer, Integer productId) {
        Product product = productRepository.findById(productId).orElse(null);
        if (product == null || customer == null) return false;

        Wishlist existing = wishlistRepository.findByCustomeridAndProductid(customer, product);
        if (existing != null) {
            wishlistRepository.delete(existing);
            return false;
        } else {
            Wishlist wishlist = new Wishlist();
            wishlist.setCustomerid(customer);
            wishlist.setProductid(product);
            wishlist.setAddedAt(Instant.now());
            wishlistRepository.save(wishlist);
            return true;
        }
    }

    public boolean isWishlisted(Customer customer, Integer productId) {
        Product product = productRepository.findById(productId).orElse(null);
        if (product == null || customer == null) return false;

        return wishlistRepository.findByCustomeridAndProductid(customer, product) != null;
    }

    public List<ProductView> getWishlistProductViews(Customer customer) {
        List<Wishlist> wishlists = wishlistRepository.findAllByCustomeridOrderByAddedAtDesc(customer);
        List<ProductView> views = new ArrayList<>();

        for (Wishlist w : wishlists) {
            Product p = w.getProductid();

            if ("available".equals(p.getStatus())) {

                if (inventoryRepository.findAllByProductid(p).size()>0) {

                    int totalSold = inventoryRepository.findAllByProductid(p)
                            .stream()
                            .mapToInt(Inventory::getSolditems)
                            .sum();

                    List<Productimage> imgs = productimageRepository.findAllByProductid(p);
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

                    views.add(new ProductView(
                            p.getId(), p.getName(), (inventoryRepository.findFirstByProductidOrderByPriceAsc(p).getPrice()), totalSold,
                            imageUrl, shopaddress, rate, categoryId, categoryName
                    ));
                }
            }
        }
        return views;
    }

}
