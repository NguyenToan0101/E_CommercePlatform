package org.example.ecommerce.service.customer.order;

import org.example.ecommerce.entity.*;
import org.example.ecommerce.repository.OrderItemsRepository;
import org.example.ecommerce.repository.OrdersRepository;
import org.example.ecommerce.repository.ProductimageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class OrderService {
    @Autowired
    private OrderItemsRepository orderItemRepository;
    @Autowired
    private ProductimageRepository productimageRepository;
    @Autowired
    private OrdersRepository orderRepository;

    public Order findById(int orderId) {
        return orderRepository.findById(orderId).orElseThrow();
    }


    public List<Order> getAllOrdersByCustomerId(Customer customer) {
        return orderRepository.findAllByCustomeridOrderByOrderdateDesc(customer);
    }

    public List<OrderItemDTO> getOrderItemsWithDetails(Order orderId) {
        List<Orderitem> items = orderItemRepository.findAllByOrderid(orderId);
        List<OrderItemDTO> dtoList = new ArrayList<>();
        for (Orderitem item : items) {
            Product product = item.getProductid();
            Inventory inventory = item.getInventoryid();

            String imageUrl = "";
            List<Productimage> images = productimageRepository.findAllByProductid(product);
            if (images != null && !images.isEmpty()) {
                imageUrl = images.get(0).getImageurl();
            }

            OrderItemDTO dto = new OrderItemDTO(
                    product.getId(),
                    product.getName(),
                    imageUrl,
                    inventory.getColor(),
                    inventory.getDimension(),
                    item.getQuantity(),
                    item.getUnitprice()
            );
            dtoList.add(dto);
        }
        return dtoList;
    }
}
