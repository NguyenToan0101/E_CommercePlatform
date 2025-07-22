package org.example.ecommerce.service.customer.order;

import org.example.ecommerce.entity.*;
import org.example.ecommerce.repository.OrderItemsRepository;
import org.example.ecommerce.repository.OrdersRepository;
import org.example.ecommerce.repository.ProductimageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;

import java.util.ArrayList;
import java.util.List;
@Service
public class OrderService {
    @Autowired
    private OrderItemsRepository orderItemRepository;
    @Autowired
    private ProductimageRepository productimageRepository;
    @Autowired
    private OrdersRepository orderRepository;
    @Autowired
    private ShippingService shippingService;



    public Order findById(int orderId) {
        return orderRepository.findById(orderId).orElseThrow();
    }


    public List<Order> getAllOrdersByCustomerId(Customer customer) {
        return orderRepository.findAllByCustomeridOrderByOrderdateDesc(customer);
    }

    public List<OrderItemDTO> getOrderItemsWithDetails(Order orderId) {
        List<Orderitem> items = orderItemRepository.findAllByOrderid(orderId);
        List<OrderItemDTO> dtoList = new ArrayList<>();
        for (Orderitem item : items) {
            Product product = item.getProductid();
            Inventory inventory = item.getInventoryid();

            String imageUrl = inventory.getImage();


            OrderItemDTO dto = new OrderItemDTO(
                    item.getId(),
                    product.getId(),
                    product.getName(),
                    imageUrl,
                    inventory.getColor(),
                    inventory.getDimension(),
                    item.getQuantity(),
                    item.getUnitprice()
            );
            dtoList.add(dto);
        }
        return dtoList;
    }



    public List<Order> findOrdersForDelivery(String search, String status) {
        if ((search == null || search.isBlank()) && (status == null || status.isBlank())) {
            return orderRepository.findAll();
        }
        return orderRepository.searchByKeywordAndStatus(search, status);
    }


    @Transactional
    public void updateDeliveryStatus(Integer orderId, String newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng với ID: " + orderId));

        order.setStatus(newStatus);
        order.setUpdatedAt(Instant.now());
        orderRepository.save(order);
    }

    public Integer getOderItemById(int oderId) {
        return orderItemRepository.findById(oderId).orElseThrow().getId();
    }
    public Integer getCustomerId(int oderId){
        return orderRepository.findById(oderId).orElseThrow().getCustomerid().getId();
    }
    public Integer getProductId(int orderId) {
        return orderItemRepository.findById(orderId).orElseThrow().getProductid().getId();
    }
    public Integer getCategoryId(int orderId) {
        return orderItemRepository.findById(orderId).orElseThrow().getProductid().getCategoryid().getId();
    }
    public Integer getInventoryId(int orderId) {
        return orderItemRepository.findById(orderId).orElseThrow().getInventoryid().getId();
    }
    public Integer getPromotionId(int orderId) {
        return orderItemRepository.findById(orderId).orElseThrow().getPromotionid();
    }

    public String getProvince(int orderId) {
        String[] province = orderRepository.findById(orderId).orElseThrow().getAddress().split(",");
        return province[province.length - 1];
    }
    public String getRegion(int orderId) {
        String[] province = orderRepository.findById(orderId).orElseThrow().getAddress().split(",");
        return shippingService.getRegion(province[province.length - 1]);
    }
    public Instant getOderDate(int orderId) {
        return orderRepository.findById(orderId).orElseThrow().getOrderdate();
    }
    public Integer getQuantity(int orderId) {
        return orderItemRepository.findById(orderId).orElseThrow().getQuantity();
    }
    public BigDecimal getUnitPrice(int orderId) {
        return orderItemRepository.findById(orderId).orElseThrow().getUnitprice();
    }
    public BigDecimal getProfit(int orderId) {


        return orderItemRepository.findById(orderId).orElseThrow().getUnitprice().multiply(new BigDecimal("0.2"))
                .setScale(2, RoundingMode.HALF_UP);

    }
    public Instant getCreatedAt(int orderId) {
        return orderRepository.findById(orderId).orElseThrow().getUpdatedAt();
    }
    public BigDecimal getCost(int orderId) {
        return orderItemRepository.findById(orderId).orElseThrow().getInventoryid().getPrice();
    }



}
