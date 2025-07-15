package org.example.ecommerce.repository;

import org.example.ecommerce.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    List<Category> findAll();
    @Query("SELECT c FROM Category c WHERE c.parentid IS NULL")
    List<Category> findRootCategories();
    List<Category> findByParentid_Id(Integer parentid);
}
