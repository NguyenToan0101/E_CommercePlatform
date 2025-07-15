package org.example.ecommerce.service.customer.order;

import org.example.ecommerce.entity.Orderitem;
import org.example.ecommerce.repository.OrderItemsRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OderItemService {
    private final OrderItemsRepository orderItemsRepository;
    public OderItemService(OrderItemsRepository orderItemsRepository) {
        this.orderItemsRepository = orderItemsRepository;
    }
    public List<Orderitem> getAllOrderItems() {
        return orderItemsRepository.findAll();
    }
}
