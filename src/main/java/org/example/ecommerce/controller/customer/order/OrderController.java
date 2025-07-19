package org.example.ecommerce.controller.customer.order;

import org.example.ecommerce.entity.Customer;
import org.example.ecommerce.entity.Order;
import org.example.ecommerce.service.customer.order.OrderItemDTO;
import org.example.ecommerce.service.customer.order.OrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @GetMapping
    public String listOrders(Model model, HttpSession session) {
        Customer customer = (Customer) session.getAttribute("customer");
        if (customer == null) {
            return "redirect:/login";
        }
        model.addAttribute("customer", customer);
        List<Order> orders = orderService.getAllOrdersByCustomerId(customer);
        model.addAttribute("orders", orders);
        return "customer/order/orders_list";
    }

    @GetMapping("/{orderId}")
    public String viewOrder(@PathVariable int orderId, Model model, HttpSession session) {
        Customer customer = (Customer) session.getAttribute("customer");
        if (customer == null) {
            return "redirect:/login";
        }
        Order order = orderService.findById(orderId);
        List<OrderItemDTO> items = orderService.getOrderItemsWithDetails(order);
        model.addAttribute("customer", customer);
        model.addAttribute("order", order);
        model.addAttribute("items", items);
        return "customer/order/view";
    }

}
