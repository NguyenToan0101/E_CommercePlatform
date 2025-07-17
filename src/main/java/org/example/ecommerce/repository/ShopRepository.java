package org.example.ecommerce.repository;

import org.example.ecommerce.entity.Shop;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShopRepository extends JpaRepository<Shop, Integer> {
    List<Shop> findByShopnameContainingIgnoreCase(String shopname);
}