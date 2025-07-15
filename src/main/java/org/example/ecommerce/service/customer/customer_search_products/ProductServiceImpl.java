package org.example.ecommerce.service.customer.customer_search_products;

import org.example.ecommerce.entity.*;
import org.example.ecommerce.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private final ProductRepository productRepo;

    @Autowired
    private final InventoryRepository inventoryRepo;

    @Autowired
    private final ProductimageRepository imageRepo;

    @Autowired
    private final ReviewRepository reviewRepo;

    @Autowired
    private final ShopRepository shopRepo;

    @Autowired
    private final WishlistRepository wishlistRepo;

    public ProductServiceImpl(ProductRepository productRepo, InventoryRepository inventoryRepo, ProductimageRepository imageRepo, ReviewRepository reviewRepo, ShopRepository shopRepo, WishlistRepository wishlistRepo) {
        this.productRepo = productRepo;
        this.inventoryRepo = inventoryRepo;
        this.imageRepo = imageRepo;
        this.reviewRepo = reviewRepo;
        this.shopRepo = shopRepo;
        this.wishlistRepo = wishlistRepo;
    }

    public ProductDetail getProductDetail(Integer productId) {
        Product product = productRepo.findById(productId).orElse(null);
        Shop shop = shopRepo.findById(product.getShopid().getId()).orElse(null);
        List<Inventory> inventories = inventoryRepo.findAllByProductid(product);
        List<Productimage> images = imageRepo.findAllByProductid(product);
        List<Review> reviews = reviewRepo.findAllByProductidOrderByCreatedatDesc(product);
        List<Wishlist> wishlists = wishlistRepo.findAllByProductid(product);
        return new ProductDetail(product, shop, inventories, images, reviews, wishlists, inventoryRepo.findFirstByProductidOrderByPriceAsc(product).getPrice());
    }
}
