package org.example.ecommerce.service;

import org.example.ecommerce.common.dto.CategoryDTO;
import org.example.ecommerce.common.mapper.CategoryMapper;
import org.example.ecommerce.entity.Category;
import org.example.ecommerce.repository.CategoryRepository;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    public CategoryService(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public CategoryDTO getCategoryDTO(Category category){
        return categoryMapper.toCategoryDTO(category);
    }
}
