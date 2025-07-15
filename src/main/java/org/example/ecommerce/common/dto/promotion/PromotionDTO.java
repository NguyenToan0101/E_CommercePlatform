package org.example.ecommerce.common.dto.promotion;

import lombok.Data;
import org.example.ecommerce.common.dto.promotion.CategoryDTO;
import org.example.ecommerce.entity.Category;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class PromotionDTO {
    private Integer id;
    private String code;
    private String description;
    private LocalDateTime startdate;
    private LocalDateTime enddate;
    private Integer perUserLimit;
    private String name;
    private String type;
    private Integer minOrderValue;
    private Integer maxDiscount;
    private Integer usageLimit;
    private Integer usageCount;
    private Double value;
    private Double revenue;
    private String status;
    private Integer orders;

    private List<CategoryDTO> categories;

    public PromotionDTO() {
    }


}

