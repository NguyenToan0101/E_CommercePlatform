
package org.example.ecommerce.common.dto.admin.complaintManagement;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@Data
@AllArgsConstructor
public class ReviewInfo {
    private Integer reviewId;
    private Integer productId;
    private String productName;
    private Integer rating;
    private String comment;
    private List<String> images;
}
