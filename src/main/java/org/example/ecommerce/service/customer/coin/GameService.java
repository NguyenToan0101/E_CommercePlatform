package org.example.ecommerce.service.customer.coin;

import org.example.ecommerce.entity.Coin;
import org.example.ecommerce.entity.Customer;
import org.example.ecommerce.repository.CoinRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GameService {
    @Autowired
    private CoinRepository coinRepository;

    public Coin getCoin(Customer customer) {
        return coinRepository.findByCustomer(customer);
    }
    public void updateCoinAfterGame(Customer customer, boolean isWin, int amount) {
        Coin coin= coinRepository.findByCustomer(customer);
        if (amount < 10 || coin.getTotalXu() < amount) {
            throw new IllegalArgumentException("Không đủ xu hoặc số tiền nhỏ hơn 10.");
        }

        if (isWin) {
            coin.setTotalXu(coin.getTotalXu() + amount);
        } else {
            coin.setTotalXu(coin.getTotalXu() - amount);
        }

        coinRepository.save(coin);
    }
}
