package org.example.ecommerce.repository;

import org.example.ecommerce.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    List<Category> findAll();

    @Query("SELECT c FROM Category c LEFT JOIN FETCH c.children WHERE c.parent IS NULL")
    List<Category> findByParentIsNullWithChildren();

    @Query("SELECT c FROM Category c LEFT JOIN FETCH c.children WHERE c.parent.id = :parentId")
    List<Category> findByParentIdWithChildren(@Param("parentId") Integer parentId);

    @Query("SELECT c FROM Category c LEFT JOIN FETCH c.parent WHERE c.id = :id")
    Optional<Category> findByIdWithParent(@Param("id") Integer id);

    @Query("SELECT c FROM Category c LEFT JOIN FETCH c.parent ORDER BY c.id")
    List<Category> findAllWithParent();

    @Query("SELECT c FROM Category c WHERE c.parent IS NULL")
    List<Category> findRootCategories();

    Optional<Category> findByCategoryname(String categoryname);
}
