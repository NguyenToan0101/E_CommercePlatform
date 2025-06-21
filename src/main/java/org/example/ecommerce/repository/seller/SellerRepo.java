package org.example.ecommerce.repository.seller;

import org.example.ecommerce.entity.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SellerRepo extends JpaRepository<Seller, Integer> {
}
