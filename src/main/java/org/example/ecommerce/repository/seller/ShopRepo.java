package org.example.ecommerce.repository.seller;

import org.example.ecommerce.entity.Shop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ShopRepo extends JpaRepository<Shop, Integer> {
    
    /**
     * Tìm shop theo seller ID
     * @param sellerId ID của seller
     * @return Shop của seller
     */
    @Query("SELECT s FROM Shop s WHERE s.sellerid.id = :sellerId")
    Shop findBySelleridId(@Param("sellerId") Integer sellerId);
}
