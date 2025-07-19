package org.example.ecommerce.service.customer.customer_search_products;


import org.example.ecommerce.service.customer.customer_product.ProductView;

import java.math.BigDecimal;
import java.util.List;

public interface SearchProductService {
    List<ProductView> getProductCategory(Integer categoryId);
    List<ProductView> searchByName(String keyword);
    List<ProductView> searchByPriceAndRate(BigDecimal priceMin, BigDecimal priceMax, Integer rates, String productsSearchKey);
    List<ProductView> searchCategoryByPriceAndRate(BigDecimal priceMin, BigDecimal priceMax, Integer rates, Integer categoryId);
}
