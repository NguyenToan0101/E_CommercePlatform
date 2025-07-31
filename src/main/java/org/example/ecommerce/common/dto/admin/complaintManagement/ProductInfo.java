package org.example.ecommerce.common.dto.admin.complaintManagement;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@Data
@AllArgsConstructor
public class ProductInfo {
    private Integer productId;
    private Integer shopId;
    private String shopName;
    private String category;
    private String name;
    private String description;
    private List<String> images;
}