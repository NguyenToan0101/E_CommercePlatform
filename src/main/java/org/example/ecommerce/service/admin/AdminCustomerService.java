package org.example.ecommerce.service.admin;

import org.example.ecommerce.common.dto.admin.userManagement.UserDTO;
import org.example.ecommerce.repository.CustomerRepository;
import org.example.ecommerce.repository.OrdersRepository;
import org.example.ecommerce.repository.ShopRepository;
import org.example.ecommerce.service.customer.cusromer_aut.CustomerServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminCustomerService {
    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private OrdersRepository ordersRepository;

    @Autowired
    private ShopRepository shopRepository;

    @Autowired
    private CustomerServiceImpl customerServiceImpl;

    public List<UserDTO> getAllCustomers() {
        return customerRepository.getAllUserDTOs();
    }



    private String convertStatus(String status) {
        switch (status) {
            case "active": return "Hoạt động";
            case "banned": return "Bị khóa";
            case "pending": return "Chờ duyệt";
            default: return "Không xác định";
        }
    }


}

