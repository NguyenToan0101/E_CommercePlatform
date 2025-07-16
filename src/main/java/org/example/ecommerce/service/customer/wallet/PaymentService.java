package org.example.ecommerce.service.customer.wallet;

import org.example.ecommerce.entity.Customer;
import org.example.ecommerce.entity.Inventory;
import org.example.ecommerce.entity.Product;
import org.example.ecommerce.entity.Promotion;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface PaymentService {
    String checkout(Customer customer, List<Integer> cartItemIds,
                    String fullname, String phone, String address);

    String checkoutRealtime(Integer freeship, Integer discount,
                            BigDecimal price, Customer customer,
                            Integer inventoryId, Integer quantity,
                            String fullname, String phone, String address);

    List<CartPreviewDTO> getCheckoutPreview(Customer customer, List<Integer> cartItemIds);

    CartPreviewDTO getCheckoutPreviewRealtime(Customer customer,
                                              Integer inventoryId, Integer quantity);

    Product getProductById(Integer productId);

    Inventory getInventoryById(Integer inventoryId);

    String getProvinceShopAddressById(Product product);

    Optional<Promotion> getPromotionById(Integer promotionId);
}