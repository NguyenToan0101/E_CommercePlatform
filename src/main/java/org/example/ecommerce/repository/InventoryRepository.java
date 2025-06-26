package org.example.ecommerce.repository;

import org.example.ecommerce.entity.Inventory;
import org.example.ecommerce.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface InventoryRepository extends JpaRepository<Inventory, Integer> {
    Inventory findInventoriesById(Integer id);

    List<Inventory> findAllByProductid(Product p);

}
