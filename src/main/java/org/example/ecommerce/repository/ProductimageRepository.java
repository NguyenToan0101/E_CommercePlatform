package org.example.ecommerce.repository;

import org.example.ecommerce.entity.Productimage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductimageRepository extends JpaRepository<Productimage, Integer> {
    List<Productimage> findProductimageById(int productid);
}
