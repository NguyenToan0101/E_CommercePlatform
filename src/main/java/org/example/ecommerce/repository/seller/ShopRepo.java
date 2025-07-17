package org.example.ecommerce.repository.seller;

import org.example.ecommerce.entity.Shop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShopRepo extends JpaRepository<Shop, Integer> {

}
