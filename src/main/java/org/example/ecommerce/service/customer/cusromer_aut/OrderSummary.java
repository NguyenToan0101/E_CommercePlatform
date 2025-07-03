package org.example.ecommerce.service.customer.cusromer_aut;

public interface OrderSummary {
    Integer getCustomerId();
    Integer getOrderCount();
    java.math.BigDecimal getTotalAmount();


}
