package org.example.ecommerce.dto;

import lombok.Data;

@Data
public class CategoryDTO {
    private Integer id;
    private String categoryname;
    private String image;
    private Integer parentId;
    private String parentName;
} 