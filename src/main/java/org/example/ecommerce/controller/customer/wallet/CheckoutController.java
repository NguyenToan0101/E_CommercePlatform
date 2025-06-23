package org.example.ecommerce.controller.customer.wallet;

import org.example.ecommerce.entity.Customer;
import org.example.ecommerce.service.customer.wallet.PaymentService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/checkout")
public class CheckoutController {
    private final PaymentService paymentService;

    public CheckoutController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

//    @PostMapping
//    public String processCheckout(@RequestParam("cartItemIds") List<Integer> cartItemIds,
//                                  HttpSession session, Model model) {
//        Customer customer = (Customer) session.getAttribute("customer");
//        if (customer == null) {
//            return "redirect:/login";
//        }
//        String result = paymentService.checkout(customer, cartItemIds);
//        if ("Thanh toán thành công".equals(result)) {
//            model.addAttribute("message", result);
//            return "/customer/wallet/checkout-success";
//        } else {
//            model.addAttribute("error", result);
//            return "/customer/wallet/checkout-fail";
//        }
//    }
}