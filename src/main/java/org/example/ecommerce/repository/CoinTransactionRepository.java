package org.example.ecommerce.repository;

import org.example.ecommerce.entity.CoinTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoinTransactionRepository extends JpaRepository<CoinTransaction, Integer> {
}
