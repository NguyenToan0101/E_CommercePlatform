package org.example.ecommerce.common.mapper;

import org.example.ecommerce.common.dto.promotion.CategoryDTO;
import org.example.ecommerce.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    @Mappings({
            @Mapping(source = "id", target = "value"),
            @Mapping(source = "categoryname", target = "label")
    })
    CategoryDTO toCategoryDTO(Category category);

    @Mappings({
            @Mapping(source = "value", target = "id"),
            @Mapping(source = "label", target = "categoryname")
    })
    Category toCategory(CategoryDTO dto);

    List<CategoryDTO> toCategoryDTO(List<Category> categories);
    List<Category> toCategoryList(List<CategoryDTO> categories);
}
