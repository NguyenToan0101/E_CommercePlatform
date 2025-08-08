package org.example.ecommerce.service.customer.coin;

import org.example.ecommerce.entity.Coin;
import org.example.ecommerce.entity.Customer;
import org.example.ecommerce.entity.Wallet;
import org.example.ecommerce.entity.WalletHistory;
import org.example.ecommerce.repository.CoinRepository;
import org.example.ecommerce.repository.WalletHistoryRepository;
import org.example.ecommerce.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class ConversionCoinService {

    @Autowired
    private CoinRepository coinRepository;

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private WalletHistoryRepository walletHistoryRepository;

    private static final int MIN_CONVERSION_AMOUNT = 1000; // Hạn mức tối thiểu 1000 xu
    private static final BigDecimal CONVERSION_RATE = BigDecimal.ONE; // 1 xu = 1₫

    public Coin getOrCreateCoin(Customer customer) {
        Coin coin = coinRepository.findByCustomer(customer);
        if (coin == null) {
            coin = new Coin();
            coin.setCustomer(customer);
            coin.setTotalXu(0);
            coinRepository.save(coin);
        }
        return coin;
    }

    @Transactional
    public BigDecimal convertCoinToWallet(Customer customer, Integer coinAmount) {
        // Validation
        if (coinAmount == null || coinAmount <= 0) {
            throw new IllegalArgumentException("Số lượng xu phải lớn hơn 0");
        }

        if (coinAmount < MIN_CONVERSION_AMOUNT) {
            throw new IllegalArgumentException("Hạn mức quy đổi tối thiểu là " + MIN_CONVERSION_AMOUNT + " xu");
        }

        // Lấy thông tin coin và wallet
        Coin coin = getOrCreateCoin(customer);
        Wallet wallet = walletRepository.findByCustomerid(customer);
        
        if (wallet == null) {
            wallet = new Wallet();
            wallet.setCustomerid(customer);
            wallet.setBalance(BigDecimal.ZERO);
            walletRepository.save(wallet);
        }

        if (coin.getTotalXu() < coinAmount) {
            throw new IllegalArgumentException("Số xu hiện có không đủ để quy đổi. Bạn có " + 
                                           coin.getTotalXu() + " xu, cần " + coinAmount + " xu");
        }

        BigDecimal moneyAmount = CONVERSION_RATE.multiply(BigDecimal.valueOf(coinAmount));

        coin.setTotalXu(coin.getTotalXu() - coinAmount);
        coinRepository.save(coin);

        wallet.setBalance(wallet.getBalance().add(moneyAmount));
        wallet.setLastUpdated(java.time.Instant.now());
        walletRepository.save(wallet);

        WalletHistory history = new WalletHistory();
        history.setWalletid(wallet);
        history.setAmount(moneyAmount);
        history.setStatus("Coin Conversion");
        history.setCreatedAt(LocalDateTime.now());
        walletHistoryRepository.save(history);

        return moneyAmount;
    }
}
