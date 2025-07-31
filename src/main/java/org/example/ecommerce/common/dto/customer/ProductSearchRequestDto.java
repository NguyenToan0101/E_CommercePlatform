package org.example.ecommerce.common.dto.customer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductSearchRequestDto {

    private String keyword;
    private String userId;
    private String shopId;
    private String categoryId;

    // Price range filters
    private Double minPrice;
    private Double maxPrice;

    // Pagination
    @Builder.Default
    private Integer offset = 0;

    @Builder.Default
    private Integer size = 20;

    // Sorting: relevance, price_asc, price_desc, newest, oldest
    @Builder.Default
    private String sortBy = "relevance";

    // Additional filters
    private List<String> statuses;
    private Boolean useVariantShipping;
}