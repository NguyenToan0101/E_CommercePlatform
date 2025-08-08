package org.example.ecommerce.repository;

import org.example.ecommerce.entity.Customer;
import org.example.ecommerce.entity.DailyCheckin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface DailyCheckinRepository extends JpaRepository<DailyCheckin, Integer> {
    boolean existsByCustomerAndCheckinDate(Customer customer, LocalDate date);
}
