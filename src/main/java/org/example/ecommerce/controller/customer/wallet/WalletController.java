package org.example.ecommerce.controller.customer.wallet;

import org.example.ecommerce.entity.Customer;
import org.example.ecommerce.entity.Wallet;
import org.example.ecommerce.repository.WalletHistoryRepository;
import org.example.ecommerce.service.customer.wallet.WalletService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriComponentsBuilder;
import vn.payos.PayOS;
import vn.payos.type.CheckoutResponseData;
import vn.payos.type.PaymentData;

import java.math.BigDecimal;

@Controller
public class WalletController {

    @Autowired
    private WalletService walletService;
    private final WalletHistoryRepository walletHistoryRepository;
    private final PayOS payOS;
    private final String payosClientId = "8472bf63-826c-4091-9160-08911d6b4268";
    private final String payosApiKey = "8d30d5ce-a7eb-46b1-8935-3a3d85ad37f4";
    private final String payosChecksumKey = "e95bfb7c10cf730fa17f952e5a94fafa307dfee58d30ecd2f875cbf9f89de057";

    public WalletController(WalletHistoryRepository walletHistoryRepository) {
        this.walletHistoryRepository = walletHistoryRepository;
        this.payOS = new PayOS(payosClientId, payosApiKey, payosChecksumKey);
    }

    @GetMapping("/wallet")
    public String viewWallet(HttpSession session, Model model) {
        Customer customer = (Customer) session.getAttribute("customer");
        if (customer == null) return "redirect:/login";

        Wallet wallet = walletService.getOrCreateWallet(customer);
        model.addAttribute("wallet", wallet);
        model.addAttribute("balance", wallet.getBalance());
        model.addAttribute("historyList", walletHistoryRepository.findAllByWalletidOrderByCreatedAtDesc(wallet));
        return "/customer/wallet/wallet";
    }

    @GetMapping("/wallet/deposit")
    public String showDepositForm(HttpSession session) {
        return session.getAttribute("customer") == null ? "redirect:/login" : "/customer/wallet/deposit";
    }

    @PostMapping("/wallet/deposit")
    public String initiateDeposit(@RequestParam("amount") BigDecimal amount, HttpSession session, HttpServletRequest request) {
        Customer customer = (Customer) session.getAttribute("customer");
        if (customer == null) return "redirect:/login";

        Long orderCode = System.currentTimeMillis();
        String description = "KH: " + customer.getId();

        try {
            PaymentData paymentData = PaymentData.builder()
                    .orderCode(orderCode)
                    .amount(amount.intValue())
                    .description(description)
                    .returnUrl("http://localhost:8080/wallet/payos-return")
                    .cancelUrl("http://localhost:8080/wallet?error=Payment cancelled")
                    .build();
            CheckoutResponseData result = payOS.createPaymentLink(paymentData);
            session.setAttribute("depositAmount", amount);
            session.setAttribute("orderCode", orderCode);
            return "redirect:" + result.getCheckoutUrl();
        } catch (Exception e) {
            System.err.println("Lỗi tạo link PayOS: " + e.getMessage());
            return "redirect:" + UriComponentsBuilder.fromPath("/wallet")
                    .queryParam("error", "Không thể tạo link thanh toán. Vui lòng thử lại sau.")
                    .build().encode().toUriString();
        }
    }

    @GetMapping("/wallet/payos-return")
    public String payosReturn(HttpServletRequest request, HttpSession session, Model model) {
        Customer customer = (Customer) session.getAttribute("customer");
        if (customer == null) return "redirect:/login";

        String status = request.getParameter("status");
        Long orderCode = (Long) session.getAttribute("orderCode");
        BigDecimal amount = (BigDecimal) session.getAttribute("depositAmount");

        if ("PAID".equalsIgnoreCase(status)) {
            try {
                walletService.processSuccessfulDeposit(customer, amount, String.valueOf(orderCode));
                session.removeAttribute("depositAmount");
                session.removeAttribute("orderCode");
                return "redirect:/wallet?success=true";
            } catch (Exception e) {
                System.err.println("Lỗi xử lý PayOS: " + e.getMessage());
                return "redirect:" + UriComponentsBuilder.fromPath("/wallet")
                        .queryParam("error", "Xử lý giao dịch lỗi")
                        .build().encode().toUriString();
            }
        }
        return "redirect:" + UriComponentsBuilder.fromPath("/wallet")
                .queryParam("error", "Thanh toán thất bại hoặc bị hủy")
                .build().encode().toUriString();
    }
}