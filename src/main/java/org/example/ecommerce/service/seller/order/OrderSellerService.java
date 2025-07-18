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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.math.BigDecimal;

@Service
public class OrderSellerService {
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
    private static final String STATUS_DA_HUY = "Đã hủy";
    private static final String STATUS_YEU_CAU_TRA_HANG = "Yêu cầu trả hàng/hoàn tiền";

    // 1. Lấy tất cả đơn hàng của shop
    public List<Order> getAllOrdersByShop(Integer shopId) {
        // Lấy tất cả đơn hàng không filter status
        return ordersRepository.findAllByShopIdAndStatus(shopId, null);
    }

    // 2. Xem chi tiết đơn hàng
    public Optional<Order> getOrderDetail(Integer orderId) {
        return ordersRepository.findById(orderId);
    }
    public List<Orderitem> getOrderItems(Integer orderId) {
        return orderItemsRepository.findAllByOrderid(ordersRepository.findById(orderId).orElse(null));
    }

    // 3. Cập nhật trạng thái đơn hàng
    public boolean updateOrderStatus(Integer orderId, String status) {
        List<String> allowedStatuses = List.of(
            STATUS_CHO_XAC_NHAN,
            STATUS_DA_XAC_NHAN,
            STATUS_CHO_LAY_HANG,
            STATUS_DA_HUY
        );
        if (!allowedStatuses.contains(status)) {
            return false;
        }
        Optional<Order> orderOpt = ordersRepository.findById(orderId);
        if (orderOpt.isPresent()) {
            Order order = orderOpt.get();
            order.setStatus(status);
            order.setUpdatedAt(Instant.now());
            ordersRepository.save(order);
            return true;
        }
        return false;
    }

    // 4. Tìm kiếm và lọc đơn hàng (theo trạng thái, ngày, tên khách, mã đơn)
    public List<Order> searchOrders(Integer shopId, String status, String keyword, Instant from, Instant to) {
        String filterStatus = null;
        if (status != null && !status.isEmpty()) {
            filterStatus = status;
        }
        List<Order> orders = ordersRepository.findAllByShopIdAndStatus(shopId, filterStatus);
        return orders.stream()
                .filter(o -> (keyword == null || keyword.isEmpty() || o.getFullname().toLowerCase().contains(keyword.toLowerCase()) || o.getId().toString().contains(keyword))
                        && (from == null || !o.getOrderdate().isBefore(from))
                        && (to == null || !o.getOrderdate().isAfter(to)))
                .toList();
    }

    // 5. Hủy đơn hàng
    public boolean cancelOrder(Integer orderId) {
        Optional<Order> orderOpt = ordersRepository.findById(orderId);
        if (orderOpt.isPresent()) {
            Order order = orderOpt.get();
            String current = order.getStatus();
            if (STATUS_CHO_XAC_NHAN.equals(current) || STATUS_DA_XAC_NHAN.equals(current) || STATUS_CHO_LAY_HANG.equals(current)) {
                return updateOrderStatus(orderId, STATUS_DA_HUY);
            } else {
                return false;
            }
        }
        return false;
    }

    // 6. Xử lý trả hàng/hoàn tiền
    public boolean requestReturnOrRefund(Integer orderId, String content) {
        Optional<Order> orderOpt = ordersRepository.findById(orderId);
        if (orderOpt.isPresent()) {
            Complaint complaint = new Complaint();
            complaint.setOrderId(orderOpt.get().getId());
            complaint.setDescription(content);
            complaint.setStatus(STATUS_YEU_CAU_TRA_HANG);
            complaint.setCreatedAt(LocalDateTime.from(Instant.now()));
            complaintRepository.save(complaint);
            updateOrderStatus(orderId, STATUS_YEU_CAU_TRA_HANG);
            return true;
        }
        return false;
    }

    public long countOrdersByShop(Integer shopId) {
        return getAllOrdersByShop(shopId).size();
    }
    public long countOrdersByStatus(Integer shopId, String status) {
        List<String> allowedStatuses = List.of(
            STATUS_CHO_XAC_NHAN,
            STATUS_DA_XAC_NHAN,
            STATUS_CHO_LAY_HANG,
            STATUS_DA_HUY,
            STATUS_YEU_CAU_TRA_HANG
        );
        if (!allowedStatuses.contains(status)) return 0;
        return getAllOrdersByShop(shopId).stream().filter(o -> status.equals(o.getStatus())).count();
    }
    public BigDecimal sumRevenueByShop(Integer shopId) {
        return BigDecimal.ZERO;
    }

    public List<Object[]> countOrdersByStatusForShop(Integer shopId) {
        return ordersRepository.countOrdersByStatusForShop(shopId);
    }

    // 8. Báo cáo hiệu suất shop
    public ShopPerformanceReport getShopPerformance(Integer shopId) {
        ShopPerformanceReport report = new ShopPerformanceReport();
        report.totalOrders = countOrdersByShop(shopId);
        report.completedOrders = 0; // Không có trạng thái hoàn thành
        report.cancelledOrders = countOrdersByStatus(shopId, STATUS_DA_HUY);
        report.returnedOrders = countOrdersByStatus(shopId, STATUS_YEU_CAU_TRA_HANG);
        report.revenue = BigDecimal.ZERO;
        return report;
    }

    public static class ShopPerformanceReport {
        public long totalOrders;
        public long completedOrders;
        public long cancelledOrders;
        public long returnedOrders;
        public BigDecimal revenue;
    }
}
