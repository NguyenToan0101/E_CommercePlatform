package org.example.ecommerce.controller.customer.wallet;

import org.example.ecommerce.entity.Customer;
import org.example.ecommerce.service.customer.wallet.CartPreviewDTO;
import org.example.ecommerce.service.customer.wallet.PaymentService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/checkout")
public class CheckoutController {
    private final PaymentService paymentService;

    public CheckoutController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public String processCheckout(@RequestParam(value = "cartItemIds",required = false) List<Integer> cartItemIds,
                                  @ModelAttribute Customer customer,
                                  HttpSession session, Model model) {
        Customer customer1 = (Customer) session.getAttribute("customer");
        if (customer1 == null) {
            return "redirect:/login";
        }
        String result = paymentService.checkout(customer1, cartItemIds,customer.getFirstname()+" "+customer.getLastname(), customer.getPhone(), customer.getAddress());
        if ("Thanh toán thành công".equals(result)) {
            model.addAttribute("message", result);
            return "/customer/wallet/checkout-success";
        } else {
            model.addAttribute("error", result);
            return "/customer/wallet/checkout-fail";
        }
    }

    @PostMapping("/realtime")
    public String checkoutRealtime(@RequestParam Integer inventory, @RequestParam Integer quantity,
                                   @ModelAttribute Customer customer,
                                   HttpSession session, Model model) {
        Customer customer1 = (Customer) session.getAttribute("customer");
        if (customer1 == null) {
            return "redirect:/login";
        }
        String result = paymentService.checkoutRealtime(customer1,inventory,quantity,customer.getFirstname()+" "+customer.getLastname(), customer.getPhone(), customer.getAddress());
        if ("Thanh toán thành công".equals(result)) {
            model.addAttribute("message", result);
            return "/customer/wallet/checkout-success";
        } else {
            model.addAttribute("error", result);
            return "/customer/wallet/checkout-fail";
        }
    }

    @GetMapping("/preview")
    public String previewCheckout(
            @RequestParam List<Integer> cartItemIds,
            Model model, HttpSession session) {
        Customer customer = (Customer) session.getAttribute("customer");
        List<CartPreviewDTO> preview = paymentService.getCheckoutPreview(customer, cartItemIds);
        model.addAttribute("customer", customer);
        model.addAttribute("items", preview);
        return "customer/wallet/checkout-preview";
    }

    @GetMapping("/preview_realtime")
    public String previewCheckoutRealtime(
            @RequestParam Integer inventory, @RequestParam Integer quantity,
            Model model, HttpSession session) {
        Customer customer = (Customer) session.getAttribute("customer");
        CartPreviewDTO preview = paymentService.getCheckoutPreviewRealtime(customer, inventory, quantity);
        model.addAttribute("items", preview);
        model.addAttribute("customer", customer);
        return "customer/wallet/checkout-preview";
    }
}