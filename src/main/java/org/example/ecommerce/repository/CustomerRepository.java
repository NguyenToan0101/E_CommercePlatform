package org.example.ecommerce.repository;

import org.example.ecommerce.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {

    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
    Customer findByEmail(String email);
    List<Customer> findByRoleAndStatus(String role, boolean status);

    List<Customer> findByIsLockedTrueAndLockedUntilBefore(LocalDateTime now);
}
