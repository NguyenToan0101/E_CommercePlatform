package org.example.ecommerce.controller.customer.delivery;


import org.example.ecommerce.entity.Order;
import org.example.ecommerce.entity.OrderFactLog;
import org.example.ecommerce.entity.Orderitem;
import org.example.ecommerce.service.admin.OderLogService;
import org.example.ecommerce.service.customer.order.OderItemService;
import org.example.ecommerce.service.customer.order.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/delivery/orders")
public class DeliveryController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private OderLogService oderLogService;
    @Autowired
    private OderItemService oderItemService;

    @GetMapping
    public String listDeliveryOrders(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String status,
            Model model
    ) {
        List<Order> orders = orderService.findOrdersForDelivery(search, status);
        orders.sort(Comparator.comparing(Order::getId));

        long totalOrders = orders.size();
        long pendingOrders = orders.stream().filter(o -> "Chờ lấy hàng".equals(o.getStatus())).count();
        long deliveringOrders = orders.stream().filter(o -> "Đang giao hàng".equals(o.getStatus())).count();
        long deliveredOrders = orders.stream().filter(o -> "Đã giao".equals(o.getStatus())).count();

        System.out.println("Oders ---" + orders);
        model.addAttribute("orders", orders);
        model.addAttribute("totalOrders", totalOrders);
        model.addAttribute("pendingOrders", pendingOrders);
        model.addAttribute("deliveringOrders", deliveringOrders);
        model.addAttribute("deliveredOrders", deliveredOrders);

        return "delivery/delivery-status";
    }


    @PostMapping("/{id}/update-status")
    public String updateDeliveryStatus(
            @PathVariable("id") Integer orderId,
            @RequestParam("status") String status,
            RedirectAttributes redirectAttributes
    ) {
        try {
            orderService.updateDeliveryStatus(orderId, status);
            if(status.equalsIgnoreCase("Đã giao")){

                Order order = orderService.findById(orderId);
                if(order != null){
                List<Orderitem> orderitems = oderItemService.getAllOrderItems();
                for (Orderitem orderitem : orderitems) {
                    if(orderitem.getOrderid().getId().equals(orderId)){
                        OrderFactLog orderLog = new OrderFactLog();
                        orderLog.setOrderId(orderId);
                        orderLog.setOrderItemId(orderitem.getId());
                        orderLog.setCustomerId(order.getCustomerid().getId());
                        orderLog.setProductId(orderitem.getProductid().getId());
                        orderLog.setCategoryId(orderitem.getProductid().getCategoryid().getId());
                        orderLog.setInventoryId(orderitem.getInventoryid().getId());
                        orderLog.setPromotionId(orderitem.getPromotionid());
                        orderLog.setProvince(orderService.getProvince(orderId));
                        orderLog.setRegion(orderService.getRegion(orderId));
                        orderLog.setOrderDate(order.getOrderdate());
                        orderLog.setQuantity(orderitem.getQuantity());
                        orderLog.setUnitPrice(orderitem.getUnitprice());
                        orderLog.setProfit(orderService.getProfit(orderId));

                        orderLog.setQuantity(orderitem.getQuantity());
                        orderLog.setCost(orderService.getCost(orderitem.getId()));
                        if(orderService.getCost(orderitem.getId()).compareTo(orderitem.getUnitprice()) > 0 ){
                            orderLog.setDiscountAmount(orderService.getCost(orderitem.getId()).subtract(orderitem.getUnitprice()));
                        }

                        oderLogService.saveOderLog(orderLog);
                    }
                }


                }

            }
            redirectAttributes.addFlashAttribute("success", "Cập nhật trạng thái thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi khi cập nhật trạng thái: " + e.getMessage());
        }

        return "redirect:/delivery/orders";
    }
}

