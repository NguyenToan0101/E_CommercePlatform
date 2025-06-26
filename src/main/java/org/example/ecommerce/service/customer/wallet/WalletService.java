package org.example.ecommerce.service.customer.wallet;

import org.example.ecommerce.entity.Customer;
import org.example.ecommerce.entity.Payment;
import org.example.ecommerce.entity.Wallet;
import org.example.ecommerce.entity.WalletHistory;
import org.example.ecommerce.repository.PaymentRepository;
import org.example.ecommerce.repository.WalletHistoryRepository;
import org.example.ecommerce.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;

@Service
public class WalletService {

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private WalletHistoryRepository walletHistoryRepository;

    public Wallet getOrCreateWallet(Customer customer) {
        Wallet wallet = walletRepository.findByCustomerid(customer);
        if (wallet == null) {
            wallet = new Wallet();
            wallet.setCustomerid(customer);
            wallet.setBalance(BigDecimal.ZERO);
            wallet = walletRepository.save(wallet);
        }
        return wallet;
    }

    public void processSuccessfulDeposit(Customer customer, BigDecimal amount, String txnRef) {
        Wallet wallet = getOrCreateWallet(customer);
        wallet.setBalance(wallet.getBalance().add(amount));
        walletRepository.save(wallet);

        WalletHistory history = new WalletHistory();
        history.setWalletid(wallet);
        history.setAmount(amount);
        history.setStatus("Increase");
        history.setCreatedAt(Instant.now());
        walletHistoryRepository.save(history);

        Payment payment = new Payment();
        payment.setWalletid(wallet);
        payment.setMethod("PayOS");
        payment.setPaymentstatus("PAID");
        payment.setPaidat(Instant.now());
        paymentRepository.save(payment);
    }
}