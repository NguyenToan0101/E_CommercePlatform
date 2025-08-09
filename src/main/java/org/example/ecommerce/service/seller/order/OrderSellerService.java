package org.example.ecommerce.service.seller.order;

import org.example.ecommerce.entity.conplaint.Complaint;
import org.example.ecommerce.repository.OrdersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.example.ecommerce.entity.Order;
import org.example.ecommerce.entity.Orderitem;
import org.example.ecommerce.repository.OrderItemsRepository;
import org.example.ecommerce.repository.seller.ShopRepo;
import org.example.ecommerce.repository.ComplaintRepository;
import org.example.ecommerce.repository.PaymentRepository;
import org.example.ecommerce.repository.ProductRepository;
import org.example.ecommerce.repository.ReviewRepository;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.logging.Logger;

@Service
public class OrderSellerService {
    private static final Logger logger = Logger.getLogger(OrderSellerService.class.getName());
    
    @Autowired
    private OrdersRepository ordersRepository;
    @Autowired
    private OrderItemsRepository orderItemsRepository;
    @Autowired
    private ShopRepo shopRepo;
    @Autowired
    private ComplaintRepository complaintRepository;
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ReviewRepository reviewRepository;

    // Danh sách trạng thái đồng bộ với frontend
    private static final String STATUS_CHO_XAC_NHAN = "Chờ xác nhận";
    private static final String STATUS_DA_XAC_NHAN = "Đã xác nhận";
    private static final String STATUS_CHO_LAY_HANG = "Chờ lấy hàng";
    private static final String STATUS_DANG_GIAO = "Đang giao hàng";
    private static final String STATUS_DA_GIAO = "Đã giao";
    private static final String STATUS_DA_HUY = "Đã hủy";
    private static final String STATUS_YEU_CAU_TRA_HANG = "Yêu cầu trả hàng/hoàn tiền";

    // 1. Lấy tất cả đơn hàng của shop
    public List<Order> getAllOrdersByShop(Integer shopId) {
        try {
            if (shopId == null) {
                logger.warning("ShopId is null");
                return new ArrayList<>();
            }
            return ordersRepository.findAllByShopIdAndStatus(shopId, null);
        } catch (Exception e) {
            logger.severe("Error getting orders for shop " + shopId + ": " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // 2. Xem chi tiết đơn hàng
    public Optional<Order> getOrderDetail(Integer orderId) {
        try {
            if (orderId == null) {
                logger.warning("OrderId is null");
                return Optional.empty();
            }
            return ordersRepository.findById(orderId);
        } catch (Exception e) {
            logger.severe("Error getting order detail for order " + orderId + ": " + e.getMessage());
            return Optional.empty();
        }
    }
    
    public List<Orderitem> getOrderItems(Integer orderId) {
        try {
            if (orderId == null) {
                logger.warning("OrderId is null");
                return new ArrayList<>();
            }
            Optional<Order> order = ordersRepository.findById(orderId);
            if (order.isPresent()) {
                return orderItemsRepository.findAllByOrderid(order.get());
            }
            return new ArrayList<>();
        } catch (Exception e) {
            logger.severe("Error getting order items for order " + orderId + ": " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // 3. Cập nhật trạng thái đơn hàng
    public boolean updateOrderStatus(Integer orderId, String status) {
        try {
            if (orderId == null || status == null) {
                logger.warning("OrderId or status is null");
                return false;
            }
            
            List<String> allowedStatuses = List.of(
                    STATUS_CHO_XAC_NHAN,
                    STATUS_DA_XAC_NHAN,
                    STATUS_CHO_LAY_HANG,
                    STATUS_DANG_GIAO,
                    STATUS_DA_GIAO,
                    STATUS_DA_HUY
            );
            
            if (!allowedStatuses.contains(status)) {
                logger.warning("Invalid status: " + status);
                return false;
            }
            
            Optional<Order> orderOpt = ordersRepository.findById(orderId);
            if (orderOpt.isPresent()) {
                Order order = orderOpt.get();
                order.setStatus(status);
                order.setUpdatedAt(LocalDateTime.now());
                ordersRepository.save(order);
                logger.info("Order " + orderId + " status updated to: " + status);
                return true;
            }
            logger.warning("Order not found: " + orderId);
            return false;
        } catch (Exception e) {
            logger.severe("Error updating order status for order " + orderId + ": " + e.getMessage());
            return false;
        }
    }

    // 4. Tìm kiếm và lọc đơn hàng
    public List<Order> searchOrders(Integer shopId, String status, String keyword, LocalDateTime from, LocalDateTime to) {
        try {
            if (shopId == null) {
                logger.warning("ShopId is null");
                return new ArrayList<>();
            }
            
            List<Order> orders;

            if (status != null && !status.isEmpty()) {
                orders = ordersRepository.findAllByShopIdAndStatus(shopId, status);
            } else {
                orders = ordersRepository.findAllByShopIdAndStatus(shopId, null);
            }

            return orders.stream()
                    .filter(o -> {
                        boolean matchesKeyword = keyword == null || keyword.isEmpty() ||
                                (o.getFullname() != null && o.getFullname().toLowerCase().contains(keyword.toLowerCase())) ||
                                (o.getId() != null && o.getId().toString().contains(keyword));
                        boolean matchesFromDate = from == null || (o.getOrderdate() != null && !o.getOrderdate().isBefore(from));
                        boolean matchesToDate = to == null || (o.getOrderdate() != null && !o.getOrderdate().isAfter(to));
                        return matchesKeyword && matchesFromDate && matchesToDate;
                    })
                    .toList();
        } catch (Exception e) {
            logger.severe("Error searching orders for shop " + shopId + ": " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // 5. Hủy đơn hàng
    public boolean cancelOrder(Integer orderId) {
        try {
            if (orderId == null) {
                logger.warning("OrderId is null");
                return false;
            }
            
            Optional<Order> orderOpt = ordersRepository.findById(orderId);
            if (orderOpt.isPresent()) {
                Order order = orderOpt.get();
                String current = order.getStatus();
                if (STATUS_CHO_XAC_NHAN.equals(current) || STATUS_DA_XAC_NHAN.equals(current) || STATUS_CHO_LAY_HANG.equals(current)) {
                    boolean result = updateOrderStatus(orderId, STATUS_DA_HUY);
                    if (result) {
                        logger.info("Order " + orderId + " cancelled successfully");
                    }
                    return result;
                } else {
                    logger.warning("Cannot cancel order " + orderId + " with status: " + current);
                    return false;
                }
            }
            logger.warning("Order not found: " + orderId);
            return false;
        } catch (Exception e) {
            logger.severe("Error cancelling order " + orderId + ": " + e.getMessage());
            return false;
        }
    }

    // 6. Xử lý trả hàng/hoàn tiền
    public boolean requestReturnOrRefund(Integer orderId, String content) {
        try {
            if (orderId == null || content == null || content.trim().isEmpty()) {
                logger.warning("OrderId or content is null/empty");
                return false;
            }
            
            Optional<Order> orderOpt = ordersRepository.findById(orderId);
            if (orderOpt.isPresent()) {
                Complaint complaint = new Complaint();
                complaint.setOrderId(orderOpt.get().getId());
                complaint.setDescription(content);
                complaint.setStatus(STATUS_YEU_CAU_TRA_HANG);
                complaint.setCreatedAt(LocalDateTime.from(Instant.now()));
                complaintRepository.save(complaint);
                updateOrderStatus(orderId, STATUS_YEU_CAU_TRA_HANG);
                logger.info("Return/refund request created for order " + orderId);
                return true;
            }
            logger.warning("Order not found: " + orderId);
            return false;
        } catch (Exception e) {
            logger.severe("Error creating return/refund request for order " + orderId + ": " + e.getMessage());
            return false;
        }
    }

    public long countOrdersByShop(Integer shopId) {
        try {
            if (shopId == null) return 0;
            return getAllOrdersByShop(shopId).size();
        } catch (Exception e) {
            logger.severe("Error counting orders for shop " + shopId + ": " + e.getMessage());
            return 0;
        }
    }
    
    public long countOrdersByStatus(Integer shopId, String status) {
        try {
            if (shopId == null || status == null) return 0;
            
            List<String> allowedStatuses = List.of(
                    STATUS_CHO_XAC_NHAN,
                    STATUS_DA_XAC_NHAN,
                    STATUS_CHO_LAY_HANG,
                    STATUS_DANG_GIAO,
                    STATUS_DA_GIAO,
                    STATUS_DA_HUY,
                    STATUS_YEU_CAU_TRA_HANG
            );
            if (!allowedStatuses.contains(status)) return 0;
            return getAllOrdersByShop(shopId).stream().filter(o -> status.equals(o.getStatus())).count();
        } catch (Exception e) {
            logger.severe("Error counting orders by status for shop " + shopId + ": " + e.getMessage());
            return 0;
        }
    }
    
    public BigDecimal sumRevenueByShop(Integer shopId) {
        try {
            if (shopId == null) return BigDecimal.ZERO;
            return ordersRepository.sumRevenueByShopId(shopId);
        } catch (Exception e) {
            logger.severe("Error calculating revenue for shop " + shopId + ": " + e.getMessage());
            return BigDecimal.ZERO;
        }
    }

    public List<Object[]> countOrdersByStatusForShop(Integer shopId) {
        try {
            if (shopId == null) return new ArrayList<>();
            return ordersRepository.countOrdersByStatusForShop(shopId);
        } catch (Exception e) {
            logger.severe("Error getting order status counts for shop " + shopId + ": " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // 8. Báo cáo hiệu suất shop
    public ShopPerformanceReport getShopPerformance(Integer shopId) {
        try {
            if (shopId == null) {
                logger.warning("ShopId is null for performance report");
                return new ShopPerformanceReport();
            }
            
            ShopPerformanceReport report = new ShopPerformanceReport();
            report.totalOrders = countOrdersByShop(shopId);
            report.completedOrders = countOrdersByStatus(shopId, STATUS_DA_GIAO);
            report.cancelledOrders = countOrdersByStatus(shopId, STATUS_DA_HUY);
            report.returnedOrders = countOrdersByStatus(shopId, STATUS_YEU_CAU_TRA_HANG);
            report.pendingOrders = countOrdersByStatus(shopId, STATUS_CHO_XAC_NHAN);
            report.confirmedOrders = countOrdersByStatus(shopId, STATUS_DA_XAC_NHAN);
            report.shippingOrders = countOrdersByStatus(shopId, STATUS_DANG_GIAO);
            report.revenue = sumRevenueByShop(shopId);

            if (report.totalOrders > 0) {
                report.completionRate = (double) report.completedOrders / report.totalOrders * 100;
            } else {
                report.completionRate = 0.0;
            }

            logger.info("Performance report generated for shop " + shopId);
            return report;
        } catch (Exception e) {
            logger.severe("Error generating performance report for shop " + shopId + ": " + e.getMessage());
            return new ShopPerformanceReport();
        }
    }

    public static class ShopPerformanceReport {
        public long totalOrders;
        public long completedOrders;
        public long cancelledOrders;
        public long returnedOrders;
        public long pendingOrders;
        public long confirmedOrders;
        public long shippingOrders;
        public BigDecimal revenue;
        public double completionRate;
    }
}