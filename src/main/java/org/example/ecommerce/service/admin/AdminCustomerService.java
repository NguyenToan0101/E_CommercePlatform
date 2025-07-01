package org.example.ecommerce.service.admin;

import org.example.ecommerce.common.dto.admin.userMagement.UserDTO;
import org.example.ecommerce.entity.Customer;
import org.example.ecommerce.entity.Shop;
import org.example.ecommerce.repository.CustomerRepository;
import org.example.ecommerce.repository.OrdersRepository;
import org.example.ecommerce.repository.ShopRepository;
import org.example.ecommerce.service.customer.cusromer_aut.CustomerServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

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
        List<Customer> customers = customerRepository.findAll();
        return customers.stream()
                .map(this::mapToUserDTO)
                .collect(Collectors.toList());
    }

    public List<UserDTO> getPendingSellers() {
        List<Shop> pendingShops = shopRepository.findPendingShops();

        if (pendingShops.isEmpty()) {
            System.out.println("Không có shop nào đang chờ duyệt.");
            return List.of();
        }

        return pendingShops.stream()
                .map(shop -> {
                    Integer customerId = shop.getId();
                    Customer customer = customerRepository.findById(customerId).orElse(null);
                    if (customer == null) return null;
                    return mapToUserDTO(customer);
                })
                .filter(dto -> dto != null)
                .collect(Collectors.toList());
    }

    private UserDTO mapToUserDTO(Customer c) {
        UserDTO dto = new UserDTO();
        dto.setCustomerid(c.getId());
        dto.setFullName(c.getFirstname() + " " + c.getLastname());
        dto.setEmail(c.getEmail());
        dto.setPhone(c.getPhone());
        dto.setRole("Seller".equals(c.getRole()) ? "Seller" : "Customer");
        dto.setStatus(c.isStatus());
        dto.setJoinedDate(
                c.getCreatedat()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate()
                        .toString()
        );

        dto.setTotalOrders(ordersRepository.countByCustomerid(c));
        dto.setTotalSpending(ordersRepository.sumTotalAmountByCustomerid(c));

        dto.setGender(c.getGender() != null ? c.getGender().toString() : null);
        dto.setDateOfBirth(c.getDateofbirth() != null ? c.getDateofbirth().toString() : null);
        dto.setAddress(c.getAddress());

        // Nếu là người bán, lấy thông tin Seller và Shop
        if ("Seller".equals(c.getRole()) && c.getSeller() != null) {
            // CCCD
            dto.setIdNumber(c.getSeller().getIdNumber());

            if (c.getSeller().getShop() != null) {
                Shop shop = c.getSeller().getShop();

                dto.setShopName(shop.getShopname());
                dto.setFullAddress(shop.getFulladdress());
                dto.setBusinessType(shop.getBusinessType());
                dto.setTaxCode(shop.getTaxCode());
                dto.setPhoneShop(shop.getPhone());
                dto.setInvoiceEmail(shop.getInvoiceEmail());
                dto.setManageName(shop.getManageName());
                dto.setStatusShop(shop.getStatus());
                dto.setExpress(shop.getExpress());
                dto.setFast(shop.getFast());
                dto.setEconomy(shop.getEconomy());
                dto.setLockerDelivery(shop.getLockerDelivery());
                dto.setBulkyItems(shop.getBulkyItems());
            }
        }

        return dto;
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

