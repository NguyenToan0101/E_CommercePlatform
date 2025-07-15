package org.example.ecommerce.common.dto.category_content;

import lombok.Data;


import java.time.LocalDateTime;
import java.util.List;

@Data
public class CategoryManagementDTO {
    private Integer id;
    private String categoryname;
    private String image;
    private Integer parentId;

    private String status;
    private LocalDateTime createAt;
    private List<CategoryManagementDTO> children;
    private Integer level;
    private Integer productsCount;
}
