package org.example.ecommerce.repository;

import org.example.ecommerce.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {

}
