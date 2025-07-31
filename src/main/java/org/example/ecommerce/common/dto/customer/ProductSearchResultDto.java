//package org.example.ecommerce.common.dto.customer;
//
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//import org.example.ecommerce.entity.ProductDocument;
//
//import java.util.List;
//import java.util.Map;
//
//@Data
//@Builder
//@NoArgsConstructor
//@AllArgsConstructor
//public class ProductSearchResultDto {
//
//    private List<ProductDocument> products;
//    private Long totalHits;
//    private Integer currentPage;
//    private Integer pageSize;
//    private Integer totalPages;
//
//    // Faceted search aggregations
//    private Map<String, Object> aggregations;
//
//    // Search metadata
//    private Long searchTime;
//    private String correctedQuery;
//
//    public Integer getTotalPages() {
//        if (totalHits == null || pageSize == null || pageSize == 0) {
//            return 0;
//        }
//        return (int) Math.ceil((double) totalHits / pageSize);
//    }
//}
