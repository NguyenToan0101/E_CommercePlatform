package org.example.ecommerce.controller.customer.wallet;

import org.example.ecommerce.entity.Customer;
import org.example.ecommerce.entity.Wallet;
import org.example.ecommerce.entity.WalletHistory;
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

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class WalletController {

    @Autowired
    private WalletService walletService;
    private final WalletHistoryRepository walletHistoryRepository;

    public WalletController(WalletHistoryRepository walletHistoryRepository) {
        this.walletHistoryRepository = walletHistoryRepository;
    }

    @GetMapping("/wallet")
    public String viewWallet(HttpSession session, Model model) {
        Customer customer = (Customer) session.getAttribute("customer");
        if (customer == null) {
            return "redirect:/login";
        }

        Wallet wallet = walletService.getOrCreateWallet(customer);
        List<WalletHistory> historyList = walletHistoryRepository.findAllByWalletidOrderByCreatedAtDesc(wallet);

        model.addAttribute("wallet", wallet);
        model.addAttribute("balance", wallet.getBalance());
        model.addAttribute("historyList", historyList);

        return "/customer/wallet/wallet";
    }

    @GetMapping("/wallet/deposit")
    public String showDepositForm(HttpSession session, Model model) {
        Customer customer = (Customer) session.getAttribute("customer");
        if (customer == null) {
            return "redirect:/login";
        }
        return "/customer/wallet/deposit";
    }

    @PostMapping("/wallet/deposit")
    public String initiateDeposit(@RequestParam("amount") BigDecimal amount, HttpSession session, HttpServletRequest request) throws UnsupportedEncodingException {
        Customer customer = (Customer) session.getAttribute("customer");
        if (customer == null) {
            return "redirect:/login";
        }

        String vnp_TmnCode = "GSF4PYNZ";
        String vnp_HashSecret = "FU87CDND8AQ2WO9AT0QFP1Y88OHGVGL3";
        String vnp_Url = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html";
        String vnp_ReturnUrl = "http://localhost:8080/wallet/vnpay-return";

        String vnp_TxnRef = String.valueOf(System.currentTimeMillis());
        String vnp_OrderInfo = "Deposit to wallet for customer: " + customer.getId();
        String vnp_OrderType = "billpayment";
        String vnp_Amount = String.valueOf(amount.multiply(new BigDecimal(100)).longValue());
        String vnp_Locale = "vn";
        String vnp_BankCode = "";
        String vnp_IpAddr = request.getRemoteAddr();

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", "2.1.0");
        vnp_Params.put("vnp_Command", "pay");
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", vnp_Amount);
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", vnp_OrderInfo);
        vnp_Params.put("vnp_OrderType", vnp_OrderType);
        vnp_Params.put("vnp_Locale", vnp_Locale);
        vnp_Params.put("vnp_ReturnUrl", vnp_ReturnUrl);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);
        vnp_Params.put("vnp_CreateDate", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));

        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        for (String fieldName : fieldNames) {
            String fieldValue = vnp_Params.get(fieldName);
            if (fieldValue != null && fieldValue.length() > 0) {
                hashData.append(fieldName).append('=').append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString())).append('&');
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString())).append('=').append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString())).append('&');
            }
        }
        String queryUrl = query.substring(0, query.length() - 1);
        String vnp_SecureHash = walletService.hmacSHA512(vnp_HashSecret, hashData.substring(0, hashData.length() - 1));
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;

        String paymentUrl = vnp_Url + "?" + queryUrl;
        session.setAttribute("depositAmount", amount);
        session.setAttribute("txnRef", vnp_TxnRef);
        return "redirect:" + paymentUrl;
    }

    @GetMapping("/wallet/vnpay-return")
    public String vnpayReturn(HttpServletRequest request, HttpSession session, Model model) {
        Customer customer = (Customer) session.getAttribute("customer");
        if (customer == null) {
            return "redirect:/login";
        }

        String vnp_ResponseCode = request.getParameter("vnp_ResponseCode");
        String txnRef = (String) session.getAttribute("txnRef");
        BigDecimal amount = (BigDecimal) session.getAttribute("depositAmount");

        if ("00".equals(vnp_ResponseCode)) {
            walletService.processSuccessfulDeposit(customer, amount, txnRef);
            session.removeAttribute("depositAmount");
            session.removeAttribute("txnRef");
            return "redirect:/wallet?success=true";
        } else {
            return "redirect:/wallet?error=Payment failed";
        }
    }
}