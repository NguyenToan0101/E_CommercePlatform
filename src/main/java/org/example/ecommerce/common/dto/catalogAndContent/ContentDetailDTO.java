package org.example.ecommerce.common.dto.catalogAndContent;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ContentDetailDTO {
    private Integer contentid;
    private String title;
    private String slug;
    private String type;
    private String status;
    private String content;
    private String author;

    private LocalDateTime updatedAt;

    private String formattedUpdatedAt;

    public ContentDetailDTO(Integer contentid, String title, String slug, String type, String status, String body, String author, LocalDateTime updatedAt) {
        this.contentid = contentid;
        this.title = title;
        this.slug = slug;
        this.type = type;
        this.status = status;
        this.content = body;
        this.author = author;
        this.updatedAt = updatedAt;
    }

    public String getFormattedUpdatedAt() {
        return formattedUpdatedAt;
    }

    public void setFormattedUpdatedAt(String formattedUpdatedAt) {
        this.formattedUpdatedAt = formattedUpdatedAt;
    }

}
