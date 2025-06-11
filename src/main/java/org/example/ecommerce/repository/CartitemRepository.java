package org.example.ecommerce.repository;

import org.example.ecommerce.entity.Cart;
import org.example.ecommerce.entity.Cartitem;
import org.example.ecommerce.entity.Inventory;
import org.example.ecommerce.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartitemRepository extends JpaRepository<Cartitem, Integer> {
    Cartitem findById(int id);
    Cartitem findCartitemByProductidAndInventoryidAndCartid(Product productid, Inventory inventoryid, Cart cartid);
    List<Cartitem> findByCartidOrderByIdDesc(Cart cartid);
    List<Cartitem> findByCartidAndIdIn(Cart cart, List<Integer> ids);
}
