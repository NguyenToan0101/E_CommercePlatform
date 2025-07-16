package org.example.ecommerce.repository;

import org.example.ecommerce.entity.Product;
import org.example.ecommerce.entity.Productimage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductimageRepository extends JpaRepository<Productimage, Integer> {
    List<Productimage> findAllByProductid(Product p);

    Optional<Productimage> findFirstByProductidOrderByIdAsc(Product productid);

}
