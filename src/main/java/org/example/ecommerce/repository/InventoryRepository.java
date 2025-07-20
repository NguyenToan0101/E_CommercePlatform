package org.example.ecommerce.repository;

import org.example.ecommerce.entity.Inventory;
import org.example.ecommerce.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface InventoryRepository extends JpaRepository<Inventory, Integer> {
    Inventory findInventoriesById(Integer id);
    List<Inventory> findAllByProductid(Product p);
    Inventory findFirstByProductidOrderByPriceAsc(Product productid);

    @Query("SELECT SUM(i.solditems) FROM Inventory i WHERE i.productid = :productid")
    Integer findSumsolditemsByProductid(@Param("productid") Product productid);

    List<Inventory> findAllByProductidAndPriceBetweenOrderByPriceAsc(Product p, BigDecimal priceMin, BigDecimal priceMax);
}
