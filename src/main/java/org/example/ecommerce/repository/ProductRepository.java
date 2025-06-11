package org.example.ecommerce.repository;


import org.example.ecommerce.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    List<Product> findAll();
    List<Product> findByNameContainingIgnoreCase(String keyword);
    Product findById(int id);
}
