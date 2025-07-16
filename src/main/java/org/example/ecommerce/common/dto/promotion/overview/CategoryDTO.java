package org.example.ecommerce.common.dto.promotion.overview;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class CategoryDTO {
    private Integer value;
    private String label;

    public CategoryDTO() {
    }

    public CategoryDTO(Integer value, String label) {
        this.value = value;
        this.label = label;
    }

    //    private String image;
//    private List<MainCategoryDTO> children;


    @Override
    public String toString() {
        return "MainCategoryDTO{" +
                "categoryid=" + value +
                ", categoryname='" + label + '\'' +
                '}';
    }
}