package org.example.ecommerce.service.customer.customer_cart;

import org.example.ecommerce.entity.Customer;

import java.util.List;

public interface CartService {
    List<CartItemDTO> getCartItemsByCustomer(Customer customer);
    // API methods cho tốc độ xử lý nhanh hơn
    boolean addToCartApi(Customer customer, int productId, int inventoryId, int quantity);
    boolean updateCartItemApi(Customer customer, int cartItemId, int quantity);
    boolean removeCartItemApi(Customer customer, int cartItemId);
}
