package org.example.ecommerce.service.seller;

import org.example.ecommerce.entity.Category;
import org.example.ecommerce.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepo;

    public CategoryService(CategoryRepository categoryRepo) {
        this.categoryRepo = categoryRepo;
    }

    public List<Category> getAllCategories() {
        List<Category> categories = categoryRepo.findAll();
        if (categories == null) return List.of();
        return categories.stream().filter(Objects::nonNull).collect(Collectors.toList());
    }

    public Category getById(Integer id) {
        return categoryRepo.findById(id).orElse(null);
    }
}
