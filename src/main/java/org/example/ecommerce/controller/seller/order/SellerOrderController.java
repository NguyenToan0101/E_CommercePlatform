package org.example.ecommerce.controller.seller.order;

import org.example.ecommerce.entity.Customer;
import org.example.ecommerce.entity.Shop;
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
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.ResponseBody;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Map;
import java.util.LinkedHashMap;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/seller/orders")
public class    SellerOrderController {
    @Autowired
    private OrderSellerService orderSellerService;

    // 1. Xem tất cả đơn hàng của shop
    @GetMapping("")
    public String listOrders(HttpSession session, Model model) {
        Customer customer = (Customer) session.getAttribute("customer");
        if (customer == null || customer.getSeller() == null || customer.getSeller().getShop() == null)
            return "redirect:/login";
        Integer shopId = customer.getSeller().getShop().getId();
        List<Order> orders = orderSellerService.getAllOrdersByShop(shopId);
        model.addAttribute("orders", orders);
        // Thêm các biến thống kê cho dashboard
        long totalOrders = orderSellerService.countOrdersByShop(shopId);
        long completedOrders = orderSellerService.countOrdersByStatus(shopId, "Đã giao");
        long cancelledOrders = orderSellerService.countOrdersByStatus(shopId, "Đã hủy");
        model.addAttribute("totalOrders", totalOrders);
        model.addAttribute("completedOrders", completedOrders);
        model.addAttribute("cancelledOrders", cancelledOrders);

        // Đảm bảo truyền đủ key cho tất cả trạng thái
        List<String> allStatuses = List.of(
            "Chờ xác nhận", "Đã xác nhận", "Chờ lấy hàng", "Đang giao", "Đã giao", "Đã hủy", "Yêu cầu trả hàng/hoàn tiền"
        );
        Map<String, Integer> statusCounts = new LinkedHashMap<>();
        for (String status : allStatuses) {
            statusCounts.put(status, 0);
        }
        for (Object[] row : orderSellerService.countOrdersByStatusForShop(shopId)) {
            String status = (String) row[0];
            Long count = (Long) row[1];
            statusCounts.put(status, count.intValue());
        }
        int total = statusCounts.values().stream().mapToInt(Integer::intValue).sum();
        statusCounts.put("Tất cả", total);
        model.addAttribute("statusCounts", statusCounts);

        return "seller/order/orders";
    }

    // 2. Xem chi tiết đơn hàng
    @GetMapping("/{orderId}")
    public String orderDetail(@PathVariable Integer orderId, Model model) {
        Optional<Order> order = orderSellerService.getOrderDetail(orderId);
        List<Orderitem> items = orderSellerService.getOrderItems(orderId);
        model.addAttribute("order", order.orElse(null));
        model.addAttribute("items", items);
        return "seller/order/order_detail";
    }

    // 3. Cập nhật trạng thái đơn hàng
    @PostMapping("/{orderId}/status")
    @ResponseBody
    public String updateOrderStatus(@PathVariable Integer orderId, @RequestParam String status) {
        // Truyền thẳng status là String
        boolean result = orderSellerService.updateOrderStatus(orderId, status);
        return result ? "success" : "fail";
    }

    // 4. Tìm kiếm và lọc đơn hàng
    @GetMapping("/search")
    public String searchOrders(HttpSession session,
                               @RequestParam(required = false) String status,
                               @RequestParam(required = false) String keyword,
                               @RequestParam(required = false) String from,
                               @RequestParam(required = false) String to,
                               Model model) {
        Customer customer = (Customer) session.getAttribute("customer");
        if (customer == null || customer.getSeller() == null || customer.getSeller().getShop() == null)
            return "redirect:/login";
        Integer shopId = customer.getSeller().getShop().getId();
        Instant fromDate = (from != null && !from.isEmpty()) ? Instant.parse(from) : null;
        Instant toDate = (to != null && !to.isEmpty()) ? Instant.parse(to) : null;
        List<Order> orders = orderSellerService.searchOrders(shopId, status, keyword, fromDate, toDate);
        model.addAttribute("orders", orders);
        // Thêm các biến thống kê cho dashboard
        long totalOrders = orderSellerService.countOrdersByShop(shopId);
        long completedOrders = orderSellerService.countOrdersByStatus(shopId, "Đã giao");
        long shippingOrders = orderSellerService.countOrdersByStatus(shopId, "Đang giao");
        java.math.BigDecimal revenue = orderSellerService.sumRevenueByShop(shopId);
        model.addAttribute("totalOrders", totalOrders);
        model.addAttribute("completedOrders", completedOrders);
        model.addAttribute("shippingOrders", shippingOrders);
        model.addAttribute("revenue", revenue);
        return "seller/order/orders";
    }

    // 5. Hủy đơn hàng
    @PostMapping("/{orderId}/cancel")
    @ResponseBody
    public String cancelOrder(@PathVariable Integer orderId) {
        boolean result = orderSellerService.cancelOrder(orderId);
        return result ? "success" : "fail";
    }

    // 6. Xử lý trả hàng/hoàn tiền
    @PostMapping("/{orderId}/return")
    @ResponseBody
    public String requestReturnOrRefund(@PathVariable Integer orderId, @RequestParam String content) {
        boolean result = orderSellerService.requestReturnOrRefund(orderId, content);
        return result ? "success" : "fail";
    }

    // 7. Thống kê đơn hàng của shop
    @GetMapping("/stats")
    @ResponseBody
    public String orderStats(HttpSession session) {
        Customer customer = (Customer) session.getAttribute("customer");
        if (customer == null || customer.getSeller() == null || customer.getSeller().getShop() == null)
            return "redirect:/login";
        Integer shopId = customer.getSeller().getShop().getId();
        long total = orderSellerService.countOrdersByShop(shopId);
        long cancelled = orderSellerService.countOrdersByStatus(shopId, "Đã hủy");
        long returned = orderSellerService.countOrdersByStatus(shopId, "Yêu cầu trả hàng/hoàn tiền");
        return String.format("Total: %d, Cancelled: %d, Returned: %d", total, cancelled, returned);
    }

    // 8. Báo cáo hiệu suất shop
    @GetMapping("/performance")
    @ResponseBody
    public OrderSellerService.ShopPerformanceReport shopPerformance(HttpSession session) {
        Customer customer = (Customer) session.getAttribute("customer");
        if (customer == null || customer.getSeller() == null || customer.getSeller().getShop() == null)
            return null;
        Integer shopId = customer.getSeller().getShop().getId();
        return orderSellerService.getShopPerformance(shopId);
    }
} 