package org.example.ecommerce.controller.customer.order;


import jakarta.servlet.http.HttpSession;
import org.example.ecommerce.entity.Customer;
import org.example.ecommerce.entity.conplaint.Complaint;
import org.example.ecommerce.entity.conplaint.ComplaintCategory;
import org.example.ecommerce.entity.conplaint.ComplaintReason;
import org.example.ecommerce.service.customer.order.CancelReturnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class CancelReturnController {
    @Autowired
    private CancelReturnService cancelReturnService;

    @Autowired
    private org.example.ecommerce.service.UploadImageFile uploadImageFile;
    @Autowired
    private org.example.ecommerce.repository.ComplaintReasonRepository complaintReasonRepository;
    @Autowired
    private org.example.ecommerce.repository.OrdersRepository ordersRepository;
    @Autowired
    private org.example.ecommerce.repository.OrderItemsRepository orderItemsRepository;

    @GetMapping("/cancel")
    public String cancelOrder(@RequestParam("id") Integer orderId, HttpSession session, RedirectAttributes redirectAttributes) {
        Customer customer = (Customer) session.getAttribute("customer");
        if (customer == null) return "redirect:/login";
        boolean result = cancelReturnService.cancelOrderByCustomer(orderId, customer);
        if (result) {
            redirectAttributes.addFlashAttribute("success", "Hủy đơn hàng thành công!");
        } else {
            redirectAttributes.addFlashAttribute("error", "Không thể hủy đơn hàng.");
        }
        return "redirect:/orders";
    }

    @PostMapping("/return-request")
    public String requestReturn(
            @RequestParam("orderItemsId") Integer orderItemsId,
            @RequestParam("category") Integer category,
            @RequestParam("reasonId") Integer reasonId,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "mediaFiles", required = false) MultipartFile[] mediaFiles,
            HttpSession session,
            RedirectAttributes redirectAttributes
    ) {
        Customer customer = (Customer) session.getAttribute("customer");
        System.out.println("[DEBUG] orderItemsId=" + orderItemsId + ", category=" + category + ", reasonId=" + reasonId + ", customer=" + (customer != null ? customer.getId() : null));
        if (customer == null) return "redirect:/login";
        boolean result = cancelReturnService.createReturnRequest(orderItemsId, category, reasonId, description, mediaFiles, customer);
        System.out.println("[DEBUG] Kết quả lưu trả hàng: " + result);
        if (result) {
            redirectAttributes.addFlashAttribute("success", "Gửi yêu cầu trả hàng thành công!");
        } else {
            redirectAttributes.addFlashAttribute("error", "Không thể gửi yêu cầu trả hàng. Vui lòng liên hệ hỗ trợ hoặc kiểm tra lại thông tin!");
        }
        return "redirect:/orders";
    }

    @GetMapping("/return-request")
    public String showReturnRequestForm(@RequestParam("orderItemsId") Integer orderItemsId, Model model, HttpSession session) {
        Customer customer = (Customer) session.getAttribute("customer");
        if (customer == null) return "redirect:/login";
        ComplaintCategory categories = cancelReturnService.getAllComplaintCategories();
        List<ComplaintReason> reasons = cancelReturnService.getAllComplaintReasons();
        model.addAttribute("customer", customer);
        model.addAttribute("orderId", orderItemsId);
        model.addAttribute("reasons", reasons);
        model.addAttribute("categories", categories);
        return "customer/order/return_request";
    }

}