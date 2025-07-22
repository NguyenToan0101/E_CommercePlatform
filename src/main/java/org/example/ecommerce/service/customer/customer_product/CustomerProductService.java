package org.example.ecommerce.service.customer.customer_product;

import org.example.ecommerce.entity.Category;
import org.example.ecommerce.entity.Shop;

import java.util.List;

public interface CustomerProductService {
    List<ProductView> getProductViews();
    List<Category> getCategories();
    List<ProductView> getProductViewsByShopId(Integer shopid);
    Shop getShops(Integer shopid);
}
