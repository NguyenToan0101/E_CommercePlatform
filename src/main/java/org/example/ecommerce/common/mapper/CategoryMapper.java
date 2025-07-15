package org.example.ecommerce.common.mapper;


import org.example.ecommerce.common.dto.category_content.CategoryManagementDTO;
import org.example.ecommerce.common.dto.category_content.ParentCategoryDTO;
import org.example.ecommerce.config.SpringContext;
import org.example.ecommerce.entity.Category;
import org.example.ecommerce.repository.CategoryRepository;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    @Mapping(source = "parent.id", target = "parentId")
    @Mapping(source = "create_at", target = "createAt")
    CategoryManagementDTO toDTO(Category category);

    List<CategoryManagementDTO> toDTOs(List<Category> categories);
    List<Category> toCategories(List<CategoryManagementDTO> categories);

    @Mapping(source = "parentId", target = "parent.id")
    @Mapping(source = "createAt", target = "create_at")
    Category toEntity(CategoryManagementDTO dto);
    @AfterMapping
    default void afterMapping(@MappingTarget CategoryManagementDTO dto, Category category) {
        if (category.getParent() != null) {
            dto.setLevel(1);
        } else {
            dto.setLevel(0);
        }

        // Gọi repository để đếm sản phẩm
        dto.setProductsCount(getCategoryRepository().countByCategoryId(category.getId()));
    }

    // 👇 Hàm helper để lấy CategoryRepository (vì không inject trực tiếp vào interface được)
    default CategoryRepository getCategoryRepository() {
        return SpringContext.getBean(CategoryRepository.class);
    }

    List<ParentCategoryDTO> toParentDTOs(List<Category> categories);
}
