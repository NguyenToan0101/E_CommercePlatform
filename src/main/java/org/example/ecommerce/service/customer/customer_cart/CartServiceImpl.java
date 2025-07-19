package org.example.ecommerce.service.customer.customer_cart;


import org.example.ecommerce.entity.*;
import org.example.ecommerce.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private final CartRepository cartRepository;
    @Autowired
    private final CartitemRepository cartitemRepository;
    @Autowired
    private final InventoryRepository inventoryRepository;
    @Autowired
    private final ProductRepository productRepository;
    @Autowired
    private final ProductimageRepository productimageRepository;

    public CartServiceImpl(CartRepository cartRepository, CartitemRepository cartitemRepository, InventoryRepository inventoryRepository, ProductRepository productRepository, ProductimageRepository productimageRepository) {
        this.cartRepository = cartRepository;
        this.cartitemRepository = cartitemRepository;
        this.inventoryRepository = inventoryRepository;
        this.productRepository = productRepository;
        this.productimageRepository = productimageRepository;
    }

    public List<CartItemDTO> getCartItemsByCustomer(Customer customer) {
        Cart cart = cartRepository.findByCustomerid(customer);
        List<CartItemDTO> result = new ArrayList<>();
        if (cart == null) return result;

        List<Cartitem> cartitems = cartitemRepository.findByCartidOrderByIdDesc(cart);
        for (Cartitem item : cartitems) {
            CartItemDTO dto = new CartItemDTO();
            dto.setCartItemId(item.getId());
            dto.setProductId(item.getProductid().getId());
            dto.setProductName(item.getProductid().getName());
            dto.setPrice(item.getInventoryid().getPrice());
            dto.setQuantity(item.getQuantity());
            dto.setColor(item.getInventoryid().getColor());
            dto.setDimension(item.getInventoryid().getDimension());
            dto.setImageUrl(item.getInventoryid().getImage());

            result.add(dto);
        }
        return result;
    }

    public void addToCart(Customer customer, int productId, int inventoryId, int quantity) {
        Cart cart = cartRepository.findByCustomerid(customer);
        if (cart == null) {
            cart = new Cart();
            cart.setCustomerid(customer);
            cartRepository.save(cart);
        }

        Product product = productRepository.findById(productId);
        Inventory inventory = inventoryRepository.findInventoriesById(inventoryId);
        Cartitem existingItem = cartitemRepository.findCartitemByProductidAndInventoryidAndCartid(product, inventory,cart);

        if (existingItem != null) {
            int newQuantity = existingItem.getQuantity() + quantity;
            existingItem.setQuantity(newQuantity);
            cartitemRepository.save(existingItem);
        } else {
            Cartitem newItem = new Cartitem();
            newItem.setCartid(cart);
            newItem.setProductid(product);
            newItem.setInventoryid(inventory);
            newItem.setQuantity(quantity);
            cartitemRepository.save(newItem);
        }
    }

    public void updateCartItem(int cartItemId, int quantity) {
        Cartitem item = cartitemRepository.findById(cartItemId).orElse(null);
        if (item != null) {
            item.setQuantity(quantity);
            cartitemRepository.save(item);
        }
    }

    public void removeCartItem(int cartItemId) {
        Cartitem item = cartitemRepository.findById(cartItemId).orElse(null);
        if (item != null) {
            cartitemRepository.delete(item);
        }
    }
}