package org.example.ecommerce.repository;

import org.example.ecommerce.entity.Shop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ShopRepository extends JpaRepository<Shop, Integer> {
    @Query("SELECT s FROM Shop s WHERE s.status = 'PENDING_APPROVAL'")
    List<Shop> findPendingShops();
}
