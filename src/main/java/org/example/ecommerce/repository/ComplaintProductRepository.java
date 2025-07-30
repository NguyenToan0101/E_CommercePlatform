package org.example.ecommerce.repository;

import org.example.ecommerce.entity.conplaint.ComplaintProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ComplaintProductRepository extends JpaRepository<ComplaintProduct,Integer> {
}
