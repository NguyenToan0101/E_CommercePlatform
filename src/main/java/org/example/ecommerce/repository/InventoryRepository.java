package org.example.ecommerce.repository;

import org.example.ecommerce.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InventoryRepository extends JpaRepository<Inventory, Integer> {
    List<Inventory> findInventoryById(Integer productid);
    Inventory findInventoriesById(Integer id);
}
