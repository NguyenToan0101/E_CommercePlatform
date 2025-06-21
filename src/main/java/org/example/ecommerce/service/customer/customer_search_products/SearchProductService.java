package org.example.ecommerce.service.customer.customer_search_products;


import org.example.ecommerce.service.customer.customer_product.ProductView;

import java.util.List;

public interface SearchProductService {
    List<ProductView> getProductCategory(Integer categoryId);
    List<ProductView> searchByName(String keyword);
}
