package org.example.ecommerce.controller.seller.complaint;

import jakarta.servlet.http.HttpSession;
import org.example.ecommerce.entity.Customer;
import org.example.ecommerce.entity.conplaint.Complaint;
import org.example.ecommerce.repository.ComplaintRepository;
import org.example.ecommerce.service.seller.complaint.RefundMoneyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/seller/refund_money")
public class RefundMoneyController {

    @Autowired private ComplaintRepository complaintRepository;
    @Autowired private RefundMoneyService refundMoneyService;

    @GetMapping("/order/{orderId}")
    public String viewComplaintsForOrder(@PathVariable Integer orderId, HttpSession session, Model model) {
        Customer customer = (Customer) session.getAttribute("customer");
        if (customer == null || customer.getSeller() == null || customer.getSeller().getShop() == null) {
            return "redirect:/login";
        }
        List<Complaint> complaints = complaintRepository.findByOrderId(orderId);
        model.addAttribute("orderId", orderId);
        model.addAttribute("complaints", complaints);
        // model.addAttribute("activePage", "promotions");
        return "seller/complaints/view_complant";
    }

    // HSMF: tất cả complaint mà shop nhận được từ đơn ở trạng thái yêu cầu trả/hoàn
    @GetMapping("/returns")
    public String viewReturnComplaintsOfShop(HttpSession session, Model model) {
        Customer customer = (Customer) session.getAttribute("customer");
        if (customer == null || customer.getSeller() == null || customer.getSeller().getShop() == null) {
            return "redirect:/login";
        }
        Integer shopId = customer.getSeller().getShop().getId();
        List<Complaint> complaints = complaintRepository.findReturnComplaintsByShopId(shopId);
        model.addAttribute("complaints", complaints);
        // model.addAttribute("activePage", "promotions");
        return "seller/complaints/view_complant";
    }

    @PostMapping("/order/{orderId}/refund")
    public String refund(@PathVariable Integer orderId, HttpSession session, Model model) {
        Customer sellerCustomer = (Customer) session.getAttribute("customer");
        if (sellerCustomer == null || sellerCustomer.getSeller() == null || sellerCustomer.getSeller().getShop() == null) {
            return "redirect:/login";
        }
        boolean ok = refundMoneyService.refundOrderToCustomer(orderId);
        // Quay về trang chi tiết đơn hàng
        return "redirect:/seller/orders/" + orderId + (ok ? "?refunded=1" : "?refunded=0");
    }
}
