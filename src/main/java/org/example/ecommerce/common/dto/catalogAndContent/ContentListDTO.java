package org.example.ecommerce.common.dto.catalogAndContent;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ContentListDTO {
    private Integer contentId;
    private String title;
    private String slug;
    private String type;
    private String status;
    private String author;
    private LocalDateTime updatedAt;

    public ContentListDTO(Integer contentId, String title, String slug, String type, String status, String author, LocalDateTime updatedAt) {
        this.contentId = contentId;
        this.title = title;
        this.slug = slug;
        this.type = type;
        this.status = status;
        this.author = author;
        this.updatedAt = updatedAt;
    }

}
