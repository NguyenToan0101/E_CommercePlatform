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
            if (item.getProductid().getShopid() != null) {
                dto.setShopId(item.getProductid().getShopid().getId());
                dto.setShopName(item.getProductid().getShopid().getShopname());
            }
            result.add(dto);
        }
        return result;
    }


    // API methods cho tốc độ xử lý nhanh hơn
    @Override
    public boolean addToCartApi(Customer customer, int productId, int inventoryId, int quantity) {
        try {
            // Tối ưu: Sử dụng direct query thay vì stream
            Cart cart = cartRepository.findByCustomerid(customer);
            if (cart == null) {
                cart = new Cart();
                cart.setCustomerid(customer);
                cartRepository.save(cart);
            }

            Product product = productRepository.findById(productId);
            if (product == null) return false;

            Inventory inventory = inventoryRepository.findById(inventoryId).orElse(null);
            if (inventory == null) return false;

            // Tối ưu: Tìm kiếm trực tiếp thay vì stream
            Cartitem existingItem = cartitemRepository.findByCartidAndProductidAndInventoryid(cart, product, inventory);

            if (existingItem != null) {
                // Cập nhật số lượng
                existingItem.setQuantity(existingItem.getQuantity() + quantity);
                cartitemRepository.save(existingItem);
                return false; // UPDATED
            } else {
                // Thêm mới
                Cartitem newItem = new Cartitem();
                newItem.setCartid(cart);
                newItem.setProductid(product);
                newItem.setInventoryid(inventory);
                newItem.setQuantity(quantity);
                cartitemRepository.save(newItem);
                return true; // ADDED
            }
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi thêm vào giỏ hàng: " + e.getMessage());
        }
    }

    @Override
    public boolean updateCartItemApi(Customer customer, int cartItemId, int quantity) {
        try {
            // Kiểm tra quyền sở hữu
            Cart cart = cartRepository.findByCustomerid(customer);
            if (cart == null) return false;

            Cartitem item = cartitemRepository.findById(cartItemId).orElse(null);
            if (item == null || !item.getCartid().equals(cart)) return false;

            if (quantity <= 0) {
                // Nếu số lượng <= 0, xóa item
                cartitemRepository.delete(item);
                return true;
            }

            item.setQuantity(quantity);
            cartitemRepository.save(item);
            return true;
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi cập nhật giỏ hàng: " + e.getMessage());
        }
    }

    @Override
    public boolean removeCartItemApi(Customer customer, int cartItemId) {
        try {
            // Kiểm tra quyền sở hữu
            Cart cart = cartRepository.findByCustomerid(customer);
            if (cart == null) return false;

            Cartitem item = cartitemRepository.findById(cartItemId).orElse(null);
            if (item == null || !item.getCartid().equals(cart)) return false;

            cartitemRepository.delete(item);
            return true;
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi xóa khỏi giỏ hàng: " + e.getMessage());
        }
    }
}