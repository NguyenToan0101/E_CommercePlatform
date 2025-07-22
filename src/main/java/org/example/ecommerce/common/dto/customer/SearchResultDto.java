package org.example.ecommerce.common.dto.customer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.ecommerce.entity.ProductDocument;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchResultDto {

    /**
     * Danh sách suggestions (autocompletion hoặc lịch sử)
     */
    private List<String> suggestions;

    /**
     * Danh sách sản phẩm tìm được
     */
    private List<ProductDocument> products;

    /**
     * Có phải là lịch sử tìm kiếm không
     */
    private boolean isHistory;

    /**
     * Tổng số kết quả tìm được
     */
    private Long totalHits;

    /**
     * Query đã được sửa lỗi chính tả (nếu có)
     */
    private String correctedQuery;

    /**
     * Thời gian thực hiện search (ms)
     */
    private Long searchTime;
}
