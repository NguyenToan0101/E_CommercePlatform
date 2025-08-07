package org.example.ecommerce.controller.customer.coin;

import org.example.ecommerce.entity.Coin;
import org.example.ecommerce.entity.Customer;
import org.example.ecommerce.entity.Wallet;
import org.example.ecommerce.service.customer.coin.ConversionCoinService;
import org.example.ecommerce.service.customer.wallet.WalletService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

@Controller
public class ConversionCoinController {

    @Autowired
    private ConversionCoinService conversionCoinService;

    @Autowired
    private WalletService walletService;

    @GetMapping("/coin/conversion")
    public String showConversionPage(HttpSession session, Model model) {
        Customer customer = (Customer) session.getAttribute("customer");
        if (customer == null) {
            return "redirect:/login";
        }

        // Lấy thông tin coin và wallet của customer
        Coin coin = conversionCoinService.getOrCreateCoin(customer);
        Wallet wallet = walletService.getOrCreateWallet(customer);

        model.addAttribute("customer", customer);
        model.addAttribute("coin", coin);
        model.addAttribute("wallet", wallet);

        return "customer/coin/conversion_coin";
    }

    @PostMapping("/coin/conversion")
    public String processConversion(HttpSession session, 
                                  Model model,
                                  @RequestParam("coinAmount") Integer coinAmount) {
        Customer customer = (Customer) session.getAttribute("customer");
        if (customer == null) {
            return "redirect:/login";
        }

        try {
            // Thực hiện quy đổi
            BigDecimal moneyAmount = conversionCoinService.convertCoinToWallet(customer, coinAmount);
            
            // Lấy thông tin cập nhật
            Coin coin = conversionCoinService.getOrCreateCoin(customer);
            Wallet wallet = walletService.getOrCreateWallet(customer);

            model.addAttribute("customer", customer);
            model.addAttribute("coin", coin);
            model.addAttribute("wallet", wallet);
            model.addAttribute("success", "Quy đổi thành công! Bạn đã nhận được ₫" + 
                             moneyAmount.toString() + " vào ví.");

        } catch (IllegalArgumentException e) {
            // Lấy thông tin hiện tại để hiển thị lại form
            Coin coin = conversionCoinService.getOrCreateCoin(customer);
            Wallet wallet = walletService.getOrCreateWallet(customer);

            model.addAttribute("customer", customer);
            model.addAttribute("coin", coin);
            model.addAttribute("wallet", wallet);
            model.addAttribute("error", e.getMessage());
            model.addAttribute("coinAmount", coinAmount);
            model.addAttribute("moneyAmount", coinAmount != null ? BigDecimal.valueOf(coinAmount) : null);
        }

        return "customer/coin/conversion_coin";
    }
}
