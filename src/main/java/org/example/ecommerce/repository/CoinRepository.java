package org.example.ecommerce.repository;

import org.example.ecommerce.entity.Coin;
import org.example.ecommerce.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CoinRepository extends JpaRepository<Coin, Integer> {
    Coin findByCustomer(Customer customer);
}
