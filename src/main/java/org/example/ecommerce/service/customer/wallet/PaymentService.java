package org.example.ecommerce.service.customer.wallet;

import org.example.ecommerce.entity.*;

import java.util.List;

public interface PaymentService {
    String checkout(Customer customer, List<Integer> cartItemIds);
}
