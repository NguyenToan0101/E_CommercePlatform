package org.example.ecommerce.service.seller.complaint;

import jakarta.transaction.Transactional;
import org.example.ecommerce.entity.*;
import org.example.ecommerce.entity.conplaint.Complaint;
import org.example.ecommerce.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class RefundMoneyService {

    @Autowired private ComplaintRepository complaintRepository;
    @Autowired private OrdersRepository ordersRepository;
    @Autowired private WalletRepository walletRepository;
    @Autowired private WalletHistoryRepository walletHistoryRepository;

    @Transactional
    public boolean refundOrderToCustomer(Integer orderId) {
        Order order = ordersRepository.findOrderById(orderId);
        if (order == null) return false;

        // Only refund paid orders
        String payStatus = order.getPaymentStatus();
        if (payStatus == null || !payStatus.equalsIgnoreCase("Đã thanh toán")) {
            return false;
        }

        BigDecimal total = order.getTotalamount();
        if (total == null) total = BigDecimal.ZERO;

        // Identify customer and seller
        Customer customer = order.getCustomerid();
        Product anyProduct = order.getOrderitems().stream().findFirst().map(Orderitem::getProductid).orElse(null);
        if (customer == null || anyProduct == null || anyProduct.getShopid() == null || anyProduct.getShopid().getSellerid() == null) {
            return false;
        }
        Customer sellerCustomer = anyProduct.getShopid().getSellerid().getCustomer();

        // Wallets
        Wallet customerWallet = walletRepository.findByCustomerid(customer);
        if (customerWallet == null) {
            customerWallet = new Wallet();
            customerWallet.setCustomerid(customer);
            customerWallet.setBalance(BigDecimal.ZERO);
        }
        Wallet sellerWallet = sellerCustomer.getWallet();
        if (sellerWallet == null) {
            sellerWallet = new Wallet();
            sellerWallet.setCustomerid(sellerCustomer);
            sellerWallet.setBalance(BigDecimal.ZERO);
        }

        BigDecimal sellerBalance = sellerWallet.getBalance() == null ? BigDecimal.ZERO : sellerWallet.getBalance();
        // Do not allow negative balance per DB constraint
        if (sellerBalance.compareTo(total) < 0) {
            return false;
        }

        // Move money: subtract from seller, add to customer
        sellerWallet.setBalance(sellerBalance.subtract(total));
        customerWallet.setBalance((customerWallet.getBalance() == null ? BigDecimal.ZERO : customerWallet.getBalance()).add(total));
        walletRepository.save(sellerWallet);
        walletRepository.save(customerWallet);

        // Histories
        WalletHistory sellerHist = new WalletHistory();
        sellerHist.setWalletid(sellerWallet);
        sellerHist.setAmount(total);
        sellerHist.setStatus("Decrease");
        sellerHist.setCreatedAt(LocalDateTime.now());
        walletHistoryRepository.save(sellerHist);

        WalletHistory customerHist = new WalletHistory();
        customerHist.setWalletid(customerWallet);
        customerHist.setAmount(total);
        customerHist.setStatus("Increase");
        customerHist.setCreatedAt(LocalDateTime.now());
        walletHistoryRepository.save(customerHist);

        // Mark related complaints resolved
        List<Complaint> complaints = complaintRepository.findByOrderId(orderId);
        for (Complaint c : complaints) {
            c.setStatus("resolved");
            c.setUpdatedAt(LocalDateTime.now());
        }
        complaintRepository.saveAll(complaints);

        // Mark order payment refunded and status updated
       
        order.setStatus("Đã hoàn tiền");
        order.setUpdatedAt(LocalDateTime.now());
        ordersRepository.save(order);
        
        return true;
    }
}
