package org.example.ecommerce.repository;

import org.example.ecommerce.entity.Cart;
import org.example.ecommerce.entity.Cartitem;
import org.example.ecommerce.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartitemRepository extends JpaRepository<Cartitem, Integer> {
    List<Cartitem> findByCartidOrderByIdDesc(Cart cartid);

//    Cartitem findCartitemByProductidAndInventoryidAndCartid(Product productid, Cart cartid);
}
