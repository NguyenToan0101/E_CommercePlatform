package org.example.ecommerce.repository;

import org.example.ecommerce.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

    @Query("SELECT COUNT(p.id) FROM Product p WHERE p.categoryid.id = :categoryId")
    int countByCategoryId(@Param("categoryId") Integer categoryId);

    List<Category> findAll();

}
