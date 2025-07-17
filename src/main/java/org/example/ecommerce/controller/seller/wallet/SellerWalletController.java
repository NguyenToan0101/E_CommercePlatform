package org.example.ecommerce.controller.seller.wallet;


import org.example.ecommerce.entity.Customer;
import org.example.ecommerce.entity.Seller;
import org.example.ecommerce.entity.Wallet;
import org.example.ecommerce.entity.WalletHistory;
import org.example.ecommerce.service.seller.wallet.SellerWalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;


import java.math.BigDecimal;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

@Controller
public class SellerWalletController {
    @Autowired
    private SellerWalletService sellerWalletService;

    @GetMapping("/seller/wallet")
    public String viewWallet(HttpSession session, Model model,
                            @RequestParam(value = "fromDate", required = false)
                            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
                            @RequestParam(value = "toDate", required = false)
                            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
                            @RequestParam(value = "page", defaultValue = "1") int page,
                            @RequestParam(value = "size", defaultValue = "10") int size) {
        Customer customer = (Customer) session.getAttribute("customer");
        if (customer == null || customer.getSeller() == null) return "redirect:/login";
        Seller seller = customer.getSeller();
        Wallet wallet = sellerWalletService.getOrCreateWalletForSeller(seller);
        if (fromDate == null || toDate == null) {
            toDate = LocalDate.now();
            fromDate = toDate.minusDays(29);
        }
        PageRequest pageable = PageRequest.of(page - 1, size);
        Page<WalletHistory> historyPage = sellerWalletService.getHistoryForSellerByDateRangePaged(seller, fromDate, toDate, pageable);
        model.addAttribute("wallet", wallet);
        model.addAttribute("balance", wallet.getBalance());
        model.addAttribute("historyPage", historyPage);
        model.addAttribute("transactionCount", historyPage.getTotalElements());
        model.addAttribute("fromDate", fromDate);
        model.addAttribute("toDate", toDate);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        return "seller/wallet";
    }

    @PostMapping("/seller/wallet/deposit")
    public String deposit(HttpSession session, @RequestParam("amount") BigDecimal amount) {
        Customer customer = (Customer) session.getAttribute("customer");
        if (customer == null || customer.getSeller() == null) return "redirect:/login";
        Seller seller = customer.getSeller();
        sellerWalletService.deposit(seller, amount);
        return "redirect:/seller/wallet";
    }

    @PostMapping("/seller/wallet/withdraw")
    public String withdraw(HttpSession session, @RequestParam("amount") BigDecimal amount, Model model) {
        Customer customer = (Customer) session.getAttribute("customer");
        if (customer == null || customer.getSeller() == null) return "redirect:/login";
        Seller seller = customer.getSeller();
        boolean success = sellerWalletService.withdraw(seller, amount);
        if (!success) {
            model.addAttribute("withdrawError", "Số dư không đủ để rút!");
        }
        return "redirect:/seller/wallet";
    }

    @GetMapping("/seller/wallet/withdraw")
    public String showWithdrawPage(HttpSession session, Model model,
                                   @RequestParam(value = "error", required = false) String error,
                                   @RequestParam(value = "success", required = false) String success) {
        Customer customer = (Customer) session.getAttribute("customer");
        if (customer == null || customer.getSeller() == null) return "redirect:/login";
        Seller seller = customer.getSeller();
        Wallet wallet = sellerWalletService.getOrCreateWalletForSeller(seller);
        model.addAttribute("balance", wallet.getBalance());
        if (error != null) model.addAttribute("withdrawError", error);
        if (success != null) model.addAttribute("withdrawSuccess", success);
        return "seller/wallet_withdraw";
    }
} 