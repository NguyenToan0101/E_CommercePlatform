package org.example.ecommerce.controller.seller.order;

import org.example.ecommerce.entity.Customer;
import org.example.ecommerce.entity.Order;
import org.example.ecommerce.entity.Orderitem;
import org.example.ecommerce.service.seller.order.OrderSellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.logging.Logger;
import java.util.HashMap;

import jakarta.servlet.http.HttpSession;
import org.example.ecommerce.repository.ProductimageRepository;

@Controller
@RequestMapping("/seller/orders")
public class SellerOrderController {
    private static final Logger logger = Logger.getLogger(SellerOrderController.class.getName());
    
    @Autowired
    private OrderSellerService orderSellerService;

    @Autowired
    private ProductimageRepository productimageRepository;

    // 1. Xem tất cả đơn hàng của shop
    @GetMapping("")
    public String listOrders(HttpSession session, Model model) {
        try {
            Customer customer = (Customer) session.getAttribute("customer");
            if (customer == null || customer.getSeller() == null || customer.getSeller().getShop() == null) {
                logger.warning("Customer not authenticated or no shop found");
                return "redirect:/login";
            }
            
            Integer shopId = customer.getSeller().getShop().getId();
            if (shopId == null) {
                logger.warning("ShopId is null for customer: " + customer.getId());
                return "redirect:/login";
            }
            
            List<Order> orders = orderSellerService.getAllOrdersByShop(shopId);
            model.addAttribute("orders", orders);
            
            // Thêm các biến thống kê cho dashboard
            OrderSellerService.ShopPerformanceReport report = orderSellerService.getShopPerformance(shopId);
            model.addAttribute("totalOrders", report.totalOrders);
            model.addAttribute("completedOrders", report.completedOrders);
            model.addAttribute("cancelledOrders", report.cancelledOrders);
            model.addAttribute("shippingOrders", report.shippingOrders);
            model.addAttribute("revenue", report.revenue);

            // Đảm bảo truyền đủ key cho tất cả trạng thái
            List<String> allStatuses = List.of(
                    "Chờ xác nhận", "Đã xác nhận", "Chờ lấy hàng",
                    "Đang giao hàng", "Đã giao", "Đã hủy", "Yêu cầu trả hàng/hoàn tiền"
            );
            Map<String, Integer> statusCounts = new LinkedHashMap<>();
            for (String status : allStatuses) {
                statusCounts.put(status, 0);
            }

            // Lấy số lượng đơn theo từng trạng thái
            statusCounts.put("Chờ xác nhận", (int) report.pendingOrders);
            statusCounts.put("Đã xác nhận", (int) report.confirmedOrders);
            statusCounts.put("Chờ lấy hàng", (int) orderSellerService.countOrdersByStatus(shopId, "Chờ lấy hàng"));
            statusCounts.put("Đang giao hàng", (int) orderSellerService.countOrdersByStatus(shopId, "Đang giao hàng"));
            statusCounts.put("Đã giao", (int) report.completedOrders);
            statusCounts.put("Đã hủy", (int) report.cancelledOrders);
            statusCounts.put("Yêu cầu trả hàng/hoàn tiền", (int) report.returnedOrders);
            int total = statusCounts.values().stream().mapToInt(Integer::intValue).sum();
            statusCounts.put("Tất cả", total);
            model.addAttribute("statusCounts", statusCounts);

            model.addAttribute("activePage", "orders-all");
            logger.info("Orders page loaded for shop: " + shopId + " with " + orders.size() + " orders");
            return "seller/order/orders";
        } catch (Exception e) {
            logger.severe("Error loading orders page: " + e.getMessage());
            model.addAttribute("error", "Có lỗi xảy ra khi tải trang đơn hàng");
            model.addAttribute("activePage", "orders-all");
            return "seller/order/orders";
        }
    }

    // 2. Xem chi tiết đơn hàng
    @GetMapping("/{orderId}")
    public String orderDetail(@PathVariable Integer orderId, Model model, HttpSession session) {
        try {
            if (orderId == null) {
                logger.warning("OrderId is null");
                return "redirect:/seller/orders";
            }
            
            Customer customer = (Customer) session.getAttribute("customer");
            if (customer == null || customer.getSeller() == null || customer.getSeller().getShop() == null) {
                logger.warning("Customer not authenticated for order detail");
                return "redirect:/login";
            }
            
            Optional<Order> order = orderSellerService.getOrderDetail(orderId);
            if (!order.isPresent()) {
                logger.warning("Order not found: " + orderId);
                model.addAttribute("error", "Không tìm thấy đơn hàng");
                model.addAttribute("activePage", "orders-all");
                return "seller/order/order_detail";
            }
            
            List<Orderitem> items = orderSellerService.getOrderItems(orderId);
            model.addAttribute("order", order.get());
            model.addAttribute("items", items);

            // Chuẩn bị map productId -> imageUrl để tránh đụng đến cột embedding khi render
            Map<Integer, String> productImageUrls = new HashMap<>();
            for (Orderitem it : items) {
                if (it.getProductid() != null && it.getProductid().getId() != null) {
                    Integer pid = it.getProductid().getId();
                    productImageUrls.computeIfAbsent(pid, id -> {
                        try {
                            String url = productimageRepository.findFirstImageUrlByProductId(id);
                            return url != null ? url : "https://via.placeholder.com/48x48?text=No+Image";
                        } catch (Exception ex) {
                            logger.warning("Cannot load image url for product " + id + ": " + ex.getMessage());
                            return "https://via.placeholder.com/48x48?text=No+Image";
                        }
                    });
                }
            }
            model.addAttribute("productImageUrls", productImageUrls);
            
            model.addAttribute("activePage", "orders-all");
            logger.info("Order detail loaded for order: " + orderId);
            return "seller/order/order_detail";
        } catch (Exception e) {
            logger.severe("Error loading order detail for order " + orderId + ": " + e.getMessage());
            model.addAttribute("error", "Có lỗi xảy ra khi tải chi tiết đơn hàng");
            model.addAttribute("activePage", "orders-all");
            return "seller/order/order_detail";
        }
    }

    // 3. Cập nhật trạng thái đơn hàng
    @PostMapping("/{orderId}/status")
    @ResponseBody
    public String updateOrderStatus(@PathVariable Integer orderId, @RequestParam String action, HttpSession session) {
        try {
            if (orderId == null || action == null || action.trim().isEmpty()) {
                logger.warning("OrderId or action is null/empty");
                return "fail";
            }
            
            Customer customer = (Customer) session.getAttribute("customer");
            if (customer == null || customer.getSeller() == null || customer.getSeller().getShop() == null) {
                logger.warning("Customer not authenticated for status update");
                return "fail";
            }
            
            Optional<Order> orderOpt = orderSellerService.getOrderDetail(orderId);
            if (!orderOpt.isPresent()) {
                logger.warning("Order not found for status update: " + orderId);
                return "fail";
            }
            
            Order order = orderOpt.get();
            String currentStatus = order.getStatus();
            String newStatus = null;

            if ("Xác nhận".equals(action) && "Chờ xác nhận".equals(currentStatus)) {
                newStatus = "Đã xác nhận";
            } else if ("Gửi hàng đi".equals(action) && "Đã xác nhận".equals(currentStatus)) {
                newStatus = "Chờ lấy hàng";
            } else {
                logger.warning("Invalid status transition: " + currentStatus + " -> " + action);
                return "fail";
            }

            boolean result = orderSellerService.updateOrderStatus(orderId, newStatus);
            if (result) {
                logger.info("Order " + orderId + " status updated from " + currentStatus + " to " + newStatus);
            }
            return result ? "success" : "fail";
        } catch (Exception e) {
            logger.severe("Error updating order status for order " + orderId + ": " + e.getMessage());
            return "fail";
        }
    }

    // 4. Tìm kiếm và lọc đơn hàng
    @GetMapping("/search")
    public String searchOrders(HttpSession session,
                               @RequestParam(required = false) String status,
                               @RequestParam(required = false) String keyword,
                               @RequestParam(required = false) String from,
                               @RequestParam(required = false) String to,
                               Model model) {
        try {
            Customer customer = (Customer) session.getAttribute("customer");
            if (customer == null || customer.getSeller() == null || customer.getSeller().getShop() == null) {
                logger.warning("Customer not authenticated for order search");
                return "redirect:/login";
            }
            
            Integer shopId = customer.getSeller().getShop().getId();
            if (shopId == null) {
                logger.warning("ShopId is null for search");
                return "redirect:/login";
            }
            
            LocalDateTime fromDate = null;
            LocalDateTime toDate = null;
            
            try {
                if (from != null && !from.isEmpty()) {
                    fromDate = LocalDateTime.parse(from);
                }
                if (to != null && !to.isEmpty()) {
                    toDate = LocalDateTime.parse(to);
                }
            } catch (Exception e) {
                logger.warning("Invalid date format in search: " + e.getMessage());
            }
            
            List<Order> orders = orderSellerService.searchOrders(shopId, status, keyword, fromDate, toDate);
            model.addAttribute("orders", orders);
            
            // Thêm các biến thống kê cho dashboard
            long totalOrders = orderSellerService.countOrdersByShop(shopId);
            long completedOrders = orderSellerService.countOrdersByStatus(shopId, "Đã giao");
            long shippingOrders = orderSellerService.countOrdersByStatus(shopId, "Đang giao hàng");
            java.math.BigDecimal revenue = orderSellerService.sumRevenueByShop(shopId);

            // Bổ sung statusCounts cho dashboard và tab trạng thái
            Map<String, Integer> statusCounts = new LinkedHashMap<>();
            statusCounts.put("Chờ xác nhận", (int) orderSellerService.countOrdersByStatus(shopId, "Chờ xác nhận"));
            statusCounts.put("Đã xác nhận", (int) orderSellerService.countOrdersByStatus(shopId, "Đã xác nhận"));
            statusCounts.put("Chờ lấy hàng", (int) orderSellerService.countOrdersByStatus(shopId, "Chờ lấy hàng"));
            statusCounts.put("Đang giao hàng", (int) orderSellerService.countOrdersByStatus(shopId, "Đang giao hàng"));
            statusCounts.put("Đã giao", (int) orderSellerService.countOrdersByStatus(shopId, "Đã giao"));
            statusCounts.put("Đã hủy", (int) orderSellerService.countOrdersByStatus(shopId, "Đã hủy"));
            statusCounts.put("Yêu cầu trả hàng/hoàn tiền", (int) orderSellerService.countOrdersByStatus(shopId, "Yêu cầu trả hàng/hoàn tiền"));
            int total = statusCounts.values().stream().mapToInt(Integer::intValue).sum();
            statusCounts.put("Tất cả", total);
            model.addAttribute("statusCounts", statusCounts);

            model.addAttribute("activePage", "orders-all");
            model.addAttribute("totalOrders", totalOrders);
            model.addAttribute("completedOrders", completedOrders);
            model.addAttribute("shippingOrders", shippingOrders);
            model.addAttribute("revenue", revenue);
            
            logger.info("Order search completed for shop " + shopId + " with " + orders.size() + " results");
            return "seller/order/orders";
        } catch (Exception e) {
            logger.severe("Error searching orders: " + e.getMessage());
            model.addAttribute("error", "Có lỗi xảy ra khi tìm kiếm đơn hàng");
            model.addAttribute("activePage", "orders-all");
            return "seller/order/orders";
        }
    }

    // 5. Hủy đơn hàng
    @PostMapping("/{orderId}/cancel")
    @ResponseBody
    public String cancelOrder(@PathVariable Integer orderId, HttpSession session) {
        try {
            if (orderId == null) {
                logger.warning("OrderId is null for cancellation");
                return "fail";
            }
            
            Customer customer = (Customer) session.getAttribute("customer");
            if (customer == null || customer.getSeller() == null || customer.getSeller().getShop() == null) {
                logger.warning("Customer not authenticated for order cancellation");
                return "fail";
            }
            
            boolean result = orderSellerService.cancelOrder(orderId);
            if (result) {
                logger.info("Order " + orderId + " cancelled successfully");
            }
            return result ? "success" : "fail";
        } catch (Exception e) {
            logger.severe("Error cancelling order " + orderId + ": " + e.getMessage());
            return "fail";
        }
    }

    // 6. Xử lý trả hàng/hoàn tiền
    @PostMapping("/{orderId}/return")
    @ResponseBody
    public String requestReturnOrRefund(@PathVariable Integer orderId, @RequestParam String content, HttpSession session) {
        try {
            if (orderId == null || content == null || content.trim().isEmpty()) {
                logger.warning("OrderId or content is null/empty for return request");
                return "fail";
            }
            
            Customer customer = (Customer) session.getAttribute("customer");
            if (customer == null || customer.getSeller() == null || customer.getSeller().getShop() == null) {
                logger.warning("Customer not authenticated for return request");
                return "fail";
            }
            
            boolean result = orderSellerService.requestReturnOrRefund(orderId, content);
            if (result) {
                logger.info("Return/refund request created for order " + orderId);
            }
            return result ? "success" : "fail";
        } catch (Exception e) {
            logger.severe("Error creating return/refund request for order " + orderId + ": " + e.getMessage());
            return "fail";
        }
    }

    // 7. Thống kê đơn hàng của shop
    @GetMapping("/stats")
    @ResponseBody
    public String orderStats(HttpSession session) {
        try {
            Customer customer = (Customer) session.getAttribute("customer");
            if (customer == null || customer.getSeller() == null || customer.getSeller().getShop() == null) {
                logger.warning("Customer not authenticated for stats");
                return "redirect:/login";
            }
            
            Integer shopId = customer.getSeller().getShop().getId();
            if (shopId == null) {
                logger.warning("ShopId is null for stats");
                return "error";
            }
            
            long total = orderSellerService.countOrdersByShop(shopId);
            long cancelled = orderSellerService.countOrdersByStatus(shopId, "Đã hủy");
            long returned = orderSellerService.countOrdersByStatus(shopId, "Yêu cầu trả hàng/hoàn tiền");
            
            logger.info("Stats generated for shop " + shopId);
            return String.format("Total: %d, Cancelled: %d, Returned: %d", total, cancelled, returned);
        } catch (Exception e) {
            logger.severe("Error generating stats: " + e.getMessage());
            return "error";
        }
    }

    // 8. Báo cáo hiệu suất shop
    @GetMapping("/performance")
    @ResponseBody
    public OrderSellerService.ShopPerformanceReport shopPerformance(HttpSession session) {
        try {
            Customer customer = (Customer) session.getAttribute("customer");
            if (customer == null || customer.getSeller() == null || customer.getSeller().getShop() == null) {
                logger.warning("Customer not authenticated for performance report");
                return null;
            }
            
            Integer shopId = customer.getSeller().getShop().getId();
            if (shopId == null) {
                logger.warning("ShopId is null for performance report");
                return null;
            }
            
            OrderSellerService.ShopPerformanceReport report = orderSellerService.getShopPerformance(shopId);
            logger.info("Performance report generated for shop " + shopId);
            return report;
        } catch (Exception e) {
            logger.severe("Error generating performance report: " + e.getMessage());
            return null;
        }
    }
}