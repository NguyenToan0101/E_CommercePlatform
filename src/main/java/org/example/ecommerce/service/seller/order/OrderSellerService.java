package org.example.ecommerce.service.seller.order;

import org.example.ecommerce.repository.OrdersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderSellerService {
    @Autowired
    private OrdersRepository ordersRepository;


}
