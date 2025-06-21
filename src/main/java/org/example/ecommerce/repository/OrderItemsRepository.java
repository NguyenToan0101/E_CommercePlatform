package org.example.ecommerce.repository;

import org.example.ecommerce.entity.Orderitem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemsRepository extends JpaRepository<Orderitem, Integer> {
}
