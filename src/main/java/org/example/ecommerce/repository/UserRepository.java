package org.example.ecommerce.repository;

import org.example.ecommerce.entity.Customer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Customer, Integer> {
//    Optional<User> findByFirtstnameAndPassword(String firstname, String password);

    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
    Customer findByEmail(String email);
}
