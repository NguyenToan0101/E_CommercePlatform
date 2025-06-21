package org.example.ecommerce.service.customer.customer_cart;

import org.example.ecommerce.entity.Customer;

import java.util.List;

public interface CartService {
    List<CartItemDTO> getCartItemsByCustomer(Customer customer);
    void addToCart(Customer customer, int productId, int inventoryId, int quantity);
    void updateCartItem(int cartItemId, int quantity);
    void removeCartItem(int cartItemId);
}
