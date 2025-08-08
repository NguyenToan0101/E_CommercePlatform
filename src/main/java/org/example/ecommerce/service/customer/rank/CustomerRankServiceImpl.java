package org.example.ecommerce.service.customer.rank;

import org.example.ecommerce.entity.CustomerRank;
import org.example.ecommerce.entity.Customer;
import org.example.ecommerce.entity.Order;
import org.example.ecommerce.repository.CustomerRankRepository;
import org.example.ecommerce.repository.CustomerRepository;
import org.example.ecommerce.repository.OrdersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerRankServiceImpl implements CustomerRankService {

    @Autowired
    private CustomerRankRepository customerRankRepository;
    
    @Autowired
    private CustomerRepository customerRepository;
    
    @Autowired
    private OrdersRepository orderRepository;

    @Override
    public CustomerRank getCustomerRank(Integer customerId) {
        Optional<CustomerRank> rank = customerRankRepository.findByCustomerId(customerId);
        return rank.orElse(null);
    }

    @Override
    public List<CustomerRank> getAllRanks() {
        return customerRankRepository.findAllOrderByPointsDesc();
    }

    @Override
    public List<CustomerRank> getRanksByType(String rankType) {
        return customerRankRepository.findByRankTypeOrderByPointsDesc(rankType);
    }

    @Override
    public void calculateAndUpdateRank(Integer customerId) {
        // Lấy thông tin khách hàng
        Customer customer = customerRepository.findById(customerId).orElse(null);
        if (customer == null) {
            return;
        }

        // Tính tổng tiền đã chi và số đơn hàng
        List<Order> orders = orderRepository.findByCustomeridId(customerId);
        Double totalSpent = orders.stream()
                .mapToDouble(order -> order.getTotalamount().doubleValue())
                .sum();
        Integer orderCount = orders.size();

        // Tính điểm dựa trên chi tiêu và số đơn hàng
        Integer points = getPointsBySpent(totalSpent, orderCount);
        
        // Xác định loại xếp hạng
        String rankType = getRankTypeByPoints(points);

        // Tìm hoặc tạo mới CustomerRank
        Optional<CustomerRank> existingRank = customerRankRepository.findByCustomerId(customerId);
        CustomerRank customerRank;
        
        if (existingRank.isPresent()) {
            customerRank = existingRank.get();
            customerRank.setUpdatedAt(Instant.now());
        } else {
            customerRank = new CustomerRank();
            customerRank.setCustomer(customer);
            customerRank.setCreatedAt(Instant.now());
            customerRank.setUpdatedAt(Instant.now());
        }

        customerRank.setPoints(points);
        customerRank.setTotalSpent(totalSpent);
        customerRank.setOrderCount(orderCount);
        customerRank.setRankType(rankType);

        customerRankRepository.save(customerRank);
    }

    @Override
    public String getRankTypeByPoints(Integer points) {
        if (points >= 10000) {
            return "KIM CƯƠNG";
        } else if (points >= 7000) {
            return "BẠCH KIM";
        } else if (points >= 5000) {
            return "VÀNG";
        } else if (points >= 2000) {
            return "BẠC";
        } else {
            return "ĐỒNG";
        }
    }

    @Override
    public Integer getPointsBySpent(Double totalSpent, Integer orderCount) {
        // Công thức tính điểm: 1 điểm cho mỗi 1000 VND chi tiêu + 50 điểm cho mỗi đơn hàng
        Integer pointsFromSpent = (int) (totalSpent / 1000);
        Integer pointsFromOrders = orderCount * 50;
        return pointsFromSpent + pointsFromOrders;
    }
}
