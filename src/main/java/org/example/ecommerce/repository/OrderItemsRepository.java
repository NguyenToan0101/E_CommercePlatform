package org.example.ecommerce.repository;

import org.example.ecommerce.entity.Order;
import org.example.ecommerce.entity.Orderitem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemsRepository extends JpaRepository<Orderitem, Integer> {
    List<Orderitem> findAllByOrderid(Order orderId);
}
