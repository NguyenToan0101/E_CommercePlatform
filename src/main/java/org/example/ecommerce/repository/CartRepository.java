package org.example.ecommerce.repository;

import org.example.ecommerce.entity.Cart;
import org.example.ecommerce.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart,Integer> {
    Cart findByCustomerid(Customer customerid);
}
