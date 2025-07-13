package org.example.ecommerce.common.dto.catalogAndContent;

public record ContentCreateDTO(
        String title,
        String slug,
        String type,
        String content,
        String status,
        Integer adminId
){}
