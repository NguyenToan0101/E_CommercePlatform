package org.example.ecommerce.service.customer.wishlist;

import org.example.ecommerce.entity.Customer;
import org.example.ecommerce.service.customer.customer_product.ProductView;

import java.util.List;

public interface WishlistService {
    boolean toggleWishlist(Customer customer, Integer productId);
    boolean isWishlisted(Customer customer, Integer productId);
    List<ProductView> getWishlistProductViews(Customer customer);
}
