package org.example.ecommerce.repository.seller;

import org.example.ecommerce.entity.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SellerRepo extends JpaRepository<Seller, Integer> {

}
