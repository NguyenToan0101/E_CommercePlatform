package org.example.ecommerce.service;

import org.example.ecommerce.common.dto.CategoryDTO;
import org.example.ecommerce.entity.Category;
import org.example.ecommerce.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;


    @Cacheable("rootCategories")
    public List<Category> getRootCategories() {
        return categoryRepository.findByParentIsNullWithChildren();
    }


    @Cacheable(value = "childCategories", key = "#parentId")
    public List<Category> getChildCategories(Integer parentId) {
        if (parentId == null) {
            return getRootCategories();
        }
        return categoryRepository.findByParentIdWithChildren(parentId);
    }


    public Category getCategoryWithParentPath(Integer id) {
        return categoryRepository.findByIdWithParent(id).orElse(null);
    }


    public CategoryDTO convertToDTO(Category category) {
        if (category == null) return null;

        CategoryDTO dto = new CategoryDTO();
        dto.setId(category.getId());
        dto.setCategoryname(category.getCategoryname());
        dto.setImage(category.getImage());


        if (category.getParent() != null) {
            try {
                dto.setParentId(category.getParent().getId());
                dto.setParentName(category.getParent().getCategoryname());
            } catch (Exception e) {
            }
        }

        return dto;
    }

    public List<CategoryDTO> convertToDTOList(List<Category> categories) {
        return categories.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }


    @Cacheable("allCategories")
    public List<CategoryDTO> getAllCategoriesForFrontend() {
        List<Category> allCategories = categoryRepository.findAllWithParent();
        return convertToDTOList(allCategories);
    }

    public boolean isCategoryOrSubcategory(Integer parentId, Integer childCategoryId) {
        Category parent = categoryRepository.findById(parentId).orElse(null);
        Category child = categoryRepository.findById(childCategoryId).orElse(null);
        if (parent == null || child == null) return false;
        while (child != null) {
            if (child.getId().equals(parent.getId())) return true;
            child = child.getParent();
        }
        return false;
    }
}
