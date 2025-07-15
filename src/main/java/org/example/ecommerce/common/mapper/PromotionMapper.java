package org.example.ecommerce.common.mapper;

import org.example.ecommerce.common.dto.promotion.CategoryDTO;
import org.example.ecommerce.common.dto.promotion.PromotionDTO;

import org.example.ecommerce.entity.Category;
import org.example.ecommerce.entity.Promotion;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.context.annotation.Bean;


import java.util.List;


@Mapper(componentModel = "spring")
public interface PromotionMapper {

//    PromotionMapper mapper = Mappers.getMapper(PromotionMapper.class);

    PromotionDTO toDTO(Promotion promotion);

    List<PromotionDTO> toDTOs(List<Promotion> promotions);

    Promotion toEntity(PromotionDTO promotionDTO);
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
