package org.example.ecommerce.repository;

import org.example.ecommerce.entity.Customer;
import org.example.ecommerce.entity.Product;
import org.example.ecommerce.entity.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WishlistRepository extends JpaRepository<Wishlist, Integer> {
    List<Wishlist> findAllByProductid(Product product);

    Wishlist findByCustomeridAndProductid(Customer customer, Product product);

    List<Wishlist> findAllByCustomeridOrderByAddedAtDesc(Customer customer);
}
