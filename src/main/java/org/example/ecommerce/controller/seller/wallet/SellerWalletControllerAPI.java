package org.example.ecommerce.controller.seller.wallet;

import jakarta.servlet.http.HttpSession;
import org.example.ecommerce.common.dto.CashflowDTO;
import org.example.ecommerce.service.seller.wallet.SellerWalletService;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.List;
import org.example.ecommerce.entity.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/seller/wallet")
public class SellerWalletControllerAPI {

    private final SellerWalletService sellerWalletService;

    @Autowired
    public SellerWalletControllerAPI(SellerWalletService sellerWalletService) {
        this.sellerWalletService = sellerWalletService;
    }

    @GetMapping("/cashflow")
    @ResponseBody
    public List<CashflowDTO> getCashflow(HttpSession session) {
        Customer customer = (Customer) session.getAttribute("customer");
        if (customer == null || customer.getSeller() == null) return List.of();
        return sellerWalletService.getCashflowForSeller(customer.getSeller());
    }
} 