package org.example.ecommerce.service.customer.wishlist;

import org.example.ecommerce.entity.*;
import org.example.ecommerce.repository.*;
import org.example.ecommerce.service.customer.customer_product.ProductView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
                String imageUrl = p.getProductimages().stream().findFirst().map(Productimage::getImageurl).orElse(null);

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
